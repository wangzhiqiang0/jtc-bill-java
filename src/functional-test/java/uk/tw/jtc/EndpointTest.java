package uk.tw.jtc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Usage;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.response.Pay;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.UsageDetailsService;
import uk.tw.jtc.utils.HttpRequestBuilder;
import uk.tw.jtc.utils.TestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
public class EndpointTest {
    @MockBean
    BillingService billingService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    UsageDetailsDao usageDetailsDao;

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    BillingDao billingDao;

    @Autowired
    UsageDetailsService usageDetailsService;

    @BeforeEach
    public void setUp() {
    }


    @Test
    public void shouldSavePhoneUsageWhenPhoneCall() {
        Mockito.when(billingService.getBillByComerId(TestUtils.CUSTOMER_ID)).thenReturn(new Billing());
        Instant incurredAt = Instant.parse("2007-07-03T10:15:30.00Z");
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(new UsageRequest(5, incurredAt));
        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/phone", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        List<Usage> usageList = usageDetailsDao.getUsageListByCustomerId(TestUtils.CUSTOMER_ID);
        Usage usage = usageList.get(0);
        assertThat(usage).isEqualTo(new Usage(TestUtils.CUSTOMER_ID, 5, UsageTypeEnum.PHONE.getType(), incurredAt));

    }

    @Test
    public void shouldSaveSMSUsageWhenSMS() {
        Mockito.when(billingService.getBillByComerId(TestUtils.CUSTOMER_ID)).thenReturn(new Billing());
        Instant incurredAt = Instant.parse("2007-07-03T10:15:30.00Z");
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(new UsageRequest(5, incurredAt));
        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/sms", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Usage> usageList = usageDetailsDao.getUsageListByCustomerId(TestUtils.CUSTOMER_ID);
        Usage usage = usageList.get(0);
        assertThat(usage).isEqualTo(new Usage(TestUtils.CUSTOMER_ID, 5, UsageTypeEnum.SMS.getType(), incurredAt));

    }

    @Test
    public void ShouldGetBasicSubscriptionFeeWhenSMSandPhoneDoesNotExceed() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        int day = LocalDate.now().lengthOfMonth() - 1;
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 1, UsageTypeEnum.SMS.getType(), Instant.parse("2021-08-16T14:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-08-16T17:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.SMS.getType(), Instant.parse("2021-08-20T14:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-09-16T14:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-09-16T17:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-07-17T00:00:00Z")));

        }};
        billingDao.createNewBill(new Billing(customerId, Instant.now().minusSeconds(60 * 60 * 24 * day), packageInfo));
        usageList.forEach(usageDetailsDao::addNewUsage);
        Map<String,Long> usagesMap = usageDetailsService.getUsageByAccountForLastMonth(LocalDate.of(2021, 9, 17), customerId);
        assertThat(usagesMap.get(UsageTypeEnum.PHONE.getType()
        )).isEqualTo(2);
        assertThat(usagesMap.get(UsageTypeEnum.SMS.getType()
        )).isEqualTo(1);


    }

    @Test
    public void ShouldGetExtraSubscriptionFeeWhenSMSandPhoneExtra() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        int day = LocalDate.now().lengthOfMonth() - 1;
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 10, UsageTypeEnum.SMS.getType(), Instant.now()));
            add(new Usage(customerId, 10, UsageTypeEnum.PHONE.getType(), Instant.now()));
            add(new Usage(customerId, 10, UsageTypeEnum.SMS.getType(), Instant.now()));
            add(new Usage(customerId, 10, UsageTypeEnum.PHONE.getType(), Instant.now()));
        }};
        billingDao.createNewBill(new Billing(customerId, Instant.now().minusSeconds(60 * 60 * 24 * day), packageInfo));
        usageList.forEach(usageDetailsDao::addNewUsage);

        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/execute", null, Object.class);
        Invoice invoice = invoiceDao.getActiveInvoice(customerId).stream().findFirst().get();
        assertThat(invoice).isEqualTo(new Invoice(customerId, new BigDecimal(53), 20, 20, Instant.now()));
    }

    @Test
    public void ShouldUsageInCurredLastMonth() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        int day = LocalDate.now().lengthOfMonth() - 1;
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 10, UsageTypeEnum.SMS.getType(), Instant.parse("2021")));
            add(new Usage(customerId, 10, UsageTypeEnum.PHONE.getType(), Instant.now()));
            add(new Usage(customerId, 10, UsageTypeEnum.SMS.getType(), Instant.now()));
            add(new Usage(customerId, 10, UsageTypeEnum.PHONE.getType(), Instant.now()));
        }};
        billingDao.createNewBill(new Billing(customerId, Instant.now().minusSeconds(60 * 60 * 24 * day), packageInfo));
        usageList.forEach(usageDetailsDao::addNewUsage);

        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/execute", null, Object.class);

    }


//    @Test
//    public void shouldGenerateInvoiceByUsageWhenExecute() {
//        List<Billing> billingList = new ArrayList<>();
//        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
//        int day = LocalDate.now().lengthOfMonth() - 1;
//        billingList.add(new Billing(UUID.randomUUID().toString(), Instant.now().minusSeconds(60 * 60 * 24 * day), packageInfo));
//        billingList.add(new Billing(UUID.randomUUID().toString(), Instant.now().minusSeconds(60 * 60 * 24 * day), packageInfo));
//        billingList.add(new Billing(UUID.randomUUID().toString(), Instant.now().minusSeconds(60 * 60 * 24 * day), packageInfo));
//        billingList.add(new Billing(UUID.randomUUID().toString(), Instant.now().minusSeconds(60 * 60 * 24 * 5), packageInfo));
//        List<String> customerList = billingList.stream().map(Billing::getCustomerId).collect(Collectors.toList());
//        Random random = new Random();
//        List<Usage> usageList = new ArrayList<>();
//        customerList.forEach(e -> {
//            usageList.add(new Usage(e, random.nextInt(100), UsageTypeEnum.SMS.getType(), Instant.now()));
//            usageList.add(new Usage(e, random.nextInt(100), UsageTypeEnum.PHONE.getType(), Instant.now()));
//            usageList.add(new Usage(e, random.nextInt(100), UsageTypeEnum.SMS.getType(), Instant.now()));
//            usageList.add(new Usage(e, random.nextInt(100), UsageTypeEnum.PHONE.getType(), Instant.now()));
//        });
//        billingList.forEach(billingDao::createNewBill);
//        usageList.forEach(usageDetailsDao::addNewUsage);
//
//        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/execute", null, Object.class);
//        customerList.forEach(e -> {
//            Invoice invoice = invoiceDao.getActiveInvoice(e).stream().findFirst().get();
//            List<Usage> usages = usageList.stream().filter(t -> e.equals(t.getCustomerId())).collect(Collectors.toList());
//            Map<String, List<Usage>> map = usages.stream().collect(Collectors.groupingBy(Usage::getType));
////            int phone = (int) map.get(UsageTypeEnum.PHONE.getType()).stream().collect(Collectors.summarizingInt(Usage::getUsage)).getSum();
////            int sms = (int) map.get(UsageTypeEnum.SMS.getType()).stream().collect(Collectors.summarizingInt(Usage::getUsage)).getSum();
////            int extraPhone = phone - packageInfo.getPhoneLimit() > 0 ? phone - packageInfo.getPhoneLimit() : 0;
////            int extraSms = sms - packageInfo.getSmsLimit() > 0 ? sms - packageInfo.getSmsLimit() : 0;
//            BigDecimal bigDecimal = packageInfo.getSubscriptionFee().add(new BigDecimal(extraPhone * packageInfo.getExtraPhoneFee().doubleValue() + extraSms * packageInfo.getExtraSMSFee().doubleValue()));
//            assertThat(invoice).isEqualTo(new Invoice(e, bigDecimal, phone, sms, Instant.now()));
//
//
//        });
//
//    }

    private List<PackageInfo> getPackageInfoList() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/package/listPackages", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JtcResponse JtcResponse = getObject((Map) response.getBody(), JtcResponse.class);
        return getObjectList((List) JtcResponse.getData(), PackageInfo.class);
    }

    @Test
    public void functionalTest() throws JsonProcessingException {
        ResponseEntity<Object> response = restTemplate.getForEntity("/package/listPackages", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JtcResponse JtcResponse = getObject((Map) response.getBody(), JtcResponse.class);
        List<PackageInfo> packageInfoList = getObjectList((List) JtcResponse.getData(), PackageInfo.class);
        PackageInfo packageInfo = packageInfoList.get(0);

        subscriptPackage(packageInfo);

        currentBillingPeriod(packageInfo, 0, 0);

        int firstPhoneUsed = 5;
        int firstSMSUsed = 5;
        usedPhone(firstPhoneUsed);
        usedSMS(firstSMSUsed);
        currentBillingPeriod(packageInfo, firstPhoneUsed, firstSMSUsed);
        execute();

        List<Invoice> invoices = getActiveInvoice();
        assertThat(invoices.size()).isEqualTo(1);
        Invoice firstInvoice = invoices.get(0);
        assertThat(firstInvoice.getPay()).isEqualTo(packageInfo.getSubscriptionFee());

        getBillAtAnyTime(packageInfo, firstPhoneUsed, firstSMSUsed);

        int secondPhoneUsed = 15;
        int secondSMSUsed = 15;
        usedPhone(secondPhoneUsed);
        usedSMS(secondSMSUsed);

        currentBillingPeriod(packageInfo, firstPhoneUsed + secondPhoneUsed, firstSMSUsed + secondSMSUsed);
        getBillAtAnyTime(packageInfo, firstPhoneUsed + secondPhoneUsed, firstSMSUsed + secondSMSUsed);

        execute();
        List<Invoice> invoices1 = getActiveInvoice();
        assertThat(invoices1.size()).isEqualTo(2);
        Invoice secondInvoice = invoices1.stream().filter(e -> !e.getInvoiceId().equals(firstInvoice.getInvoiceId())).findFirst().get();
        assertThat(secondInvoice.getPay().doubleValue()).isEqualTo(getInvoicePay(packageInfo, firstPhoneUsed + secondPhoneUsed, firstSMSUsed + secondSMSUsed));

    }

    private List<Invoice> getActiveInvoice() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange("/invoice/active", HttpMethod.GET, entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JtcResponse JtcResponse = getObject((Map) response.getBody(), JtcResponse.class);
        List<Invoice> invoices = getObjectList((List<Map>) JtcResponse.getData(), Invoice.class);

        return invoices;


    }

    private void getBillAtAnyTime(PackageInfo packageInfo, int phoneUsed, int smsUsed) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange("/billing/getInvoiceAnyTime", HttpMethod.GET, entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JtcResponse<Pay> JtcResponse = getObject((Map) response.getBody(), JtcResponse.class);
        Pay pay = getObject((Map) JtcResponse.getData(), Pay.class);

        assertThat(pay.getPay().doubleValue()).isEqualTo(getInvoicePay(packageInfo, phoneUsed, smsUsed));
    }

    private double getInvoicePay(PackageInfo packageInfo, int phoneUsed, int smsUsed) {
        double phoneExcepted = phoneUsed < packageInfo.getPhoneLimit() ? 0 : packageInfo.getExtraPhoneFee().doubleValue() * (phoneUsed - packageInfo.getPhoneLimit());
        double smsExcepted = smsUsed < packageInfo.getSmsLimit() ? 0 : packageInfo.getExtraSMSFee().doubleValue() * (smsUsed - packageInfo.getPhoneLimit());
        return packageInfo.getSubscriptionFee().doubleValue() + phoneExcepted + smsExcepted;
    }

    private void execute() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity("/billing/execute", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void usedSMS(int smsUsed) {
        Usage used = new Usage();
        used.setUsage(smsUsed);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity(used, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity("/billing/usedSMS", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void usedPhone(int phoneUsed) {
        Usage used = new Usage();
        //used.setPhoneUsed(phoneUsed);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity(used, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity("/billing/usedPhone", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void subscriptPackage(PackageInfo packageInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity("/billing/subscriptPackage/" + packageInfo.getPackageId(), entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void currentBillingPeriod(PackageInfo packageInfo, int usedPhone, int usedSms) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange("/billing/currentBillingPeriod", HttpMethod.GET, entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JtcResponse JtcResponse = getObject((Map) response.getBody(), JtcResponse.class);
        CurrentBillingAllowance allowance = getObject((Map) JtcResponse.getData(), CurrentBillingAllowance.class);
        int phoneExcepted = usedPhone < packageInfo.getPhoneLimit() ? packageInfo.getPhoneLimit() - usedPhone : 0;
        int smsExcepted = usedSms < packageInfo.getSmsLimit() ? packageInfo.getSmsLimit() - usedPhone : 0;
        assertThat(allowance.getSmsAllowance()).isEqualTo(smsExcepted);
        assertThat(allowance.getPhoneAllowance()).isEqualTo(phoneExcepted);

    }

    private <T> List<T> getObjectList(List<Map> body, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        body.stream().forEach(e -> {
            list.add(getObject(e, clazz));
        });
        return list;
    }

    private <T> T getObject(Map e, Class<T> clazz) {
        return mapper.convertValue(e, clazz);
    }


}
