package uk.tw.jtc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import uk.tw.jtc.dao.PaymentDao;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.*;
import uk.tw.jtc.request.PaymentRequest;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.response.CurrentUsageAllowance;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.response.Pay;
import uk.tw.jtc.service.SubscriptService;
import uk.tw.jtc.utils.HttpRequestBuilder;
import uk.tw.jtc.utils.TestUtils;
import uk.tw.jtc.utis.JtcTime;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
public class EndpointTest {
    @Autowired
    SubscriptService subscriptService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    UsageDetailsDao usageDetailsDao;

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    SubscriptDao subscriptDao;




    @BeforeEach
    public void setUp() {
    }


    @Test
    public void shouldSavePhoneUsageWhenPhoneCall() {
        String customerId = UUID.randomUUID().toString();
        subscriptDao.createNewSubscript(new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), TestUtils.packageInfoList.get(0)));
        Instant incurredAt = Instant.parse("2007-07-03T10:15:30.00Z");
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(new UsageRequest(5, incurredAt),customerId);
        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/phone", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        List<Usage> usageList = usageDetailsDao.getUsageListByCustomerId(customerId);
        Usage usage = usageList.get(0);
        assertThat(usage).isEqualTo(new Usage(customerId, 5, UsageTypeEnum.PHONE.getType(), incurredAt));

    }

    @Test
    public void shouldSaveSMSUsageWhenSMS() {

        String customerId = UUID.randomUUID().toString();
        subscriptDao.createNewSubscript(new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), TestUtils.packageInfoList.get(0)));
        Instant incurredAt = Instant.parse("2007-07-03T10:15:30.00Z");
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(new UsageRequest(5, incurredAt),customerId);
        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/sms", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Usage> usageList = usageDetailsDao.getUsageListByCustomerId(customerId);
        Usage usage = usageList.get(0);
        assertThat(usage).isEqualTo(new Usage(customerId, 5, UsageTypeEnum.SMS.getType(), incurredAt));

    }



    @Test
    public void ShouldGetExtraSubscriptionFeeWhenSMSandPhoneExtra() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 10, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 10, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 10, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 10, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
        }};
        subscriptDao.createNewSubscript(new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), packageInfo));
        usageList.forEach(usageDetailsDao::addNewUsage);

        ResponseEntity<Object> response = restTemplate.postForEntity("/invoice/execute", null, Object.class);
        Invoice invoice = invoiceDao.getActiveInvoice(customerId).stream().findFirst().get();
        assertThat(invoice).isEqualTo(new Invoice(customerId, new BigDecimal(53), 20, 20, Instant.now()));
    }

    @Test
    public void ShouldUsageInCurredLastMonth() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 1, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 1, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
        }};
        subscriptDao.createNewSubscript(new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), packageInfo));
        usageList.forEach(usageDetailsDao::addNewUsage);

        ResponseEntity<Object> response = restTemplate.postForEntity("/invoice/execute", null, Object.class);
        Invoice invoice = invoiceDao.getActiveInvoice(customerId).stream().findFirst().get();
        assertThat(invoice).isEqualTo(new Invoice(customerId, new BigDecimal(38), 2, 2, Instant.now()));
    }

    @Test
    public void ShouldSubscriptPackageWhenCall() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        ResponseEntity<Object> response = restTemplate.postForEntity("/subscript/subscriptPackage/"+packageInfo.getPackageId(), HttpRequestBuilder.getUsageHttpEntity(null,customerId), Object.class);
        Subscript subscript = new Subscript(customerId, Instant.parse("2021-06-17T15:00:00Z"), packageInfo);
        Subscript subscript1 =subscriptDao.getSubscriptByCustomerId(customerId);
        subscript1.setSubscriptTime(Instant.parse("2021-06-17T15:00:00Z"));
        assertThat(subscript).isEqualTo(subscript1);
        Invoice invoice = invoiceDao.getActiveInvoice(customerId).stream().findFirst().get();
        assertThat(invoice).isEqualTo(new Invoice(customerId, new BigDecimal(38), 0, 0, Instant.now()));

    }

    @Test
    public void shouldSavePaymentWhenCustomerPay() {
        String customerId = UUID.randomUUID().toString();
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), TestUtils.packageInfoList.get(0));
        subscriptDao.createNewSubscript(subscript);
        Invoice invoice = new Invoice(subscript.getCustomerId(), subscript.getPackageInfo().getSubscriptionFee(), 0, 0, Instant.parse("2021-06-17T15:00:00Z"));
        invoiceDao.createNewInvoice(invoice);

        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(new PaymentRequest(invoice.getInvoiceId(), new BigDecimal(38)),customerId);
        ResponseEntity<Object> response = restTemplate.postForEntity("/invoice/paid", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Payment> payments = paymentDao.getSubscriptByCustomerId(customerId);
        Payment payment = payments.get(0);
        Payment payment1 = new Payment(invoice.getInvoiceId(),customerId, subscript.getPackageInfo().getSubscriptionFee());
        payment1.setPaymentId(payment.getPaymentId());
        payment1.setCreatedAt(payment.getCreatedAt());
        assertThat(payment).isEqualTo(payment1);
    }

    @Test
    public void shouldGetActiveInvoiceCall() {
        String customerId = UUID.randomUUID().toString();
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), TestUtils.packageInfoList.get(0));
        subscriptDao.createNewSubscript(subscript);
        Invoice invoice1 = new Invoice(subscript.getCustomerId(), subscript.getPackageInfo().getSubscriptionFee(), 0, 0, Instant.parse("2021-06-17T15:00:00Z"));
        invoiceDao.createNewInvoice(invoice1);
        Invoice invoice2 = new Invoice(subscript.getCustomerId(), subscript.getPackageInfo().getSubscriptionFee(), 0, 0, Instant.parse("2021-06-17T15:00:00Z"));
        invoiceDao.createNewInvoice(invoice2);
        paymentDao.createPayment(new Payment(invoice1.getInvoiceId(),customerId, subscript.getPackageInfo().getSubscriptionFee()));
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(null,customerId);
        ResponseEntity<Object> response = restTemplate.exchange("/invoice/active", HttpMethod.GET,entity, Object.class);
        JtcResponse jtcResponse = getObject((Map) response.getBody(),JtcResponse.class);
        List<Invoice> invoiceList = getObjectList((List<Map>) jtcResponse.getData(),Invoice.class);
        List expected= new ArrayList();
        expected.add(invoice2);
        assertThat(invoiceList).isEqualTo(expected);
    }

    @Test
    public void ShouldGetCurrentUsageAllowance() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now())));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        CurrentUsageAllowance exceptedCurrentUsageAllowance = new CurrentUsageAllowance(8,6);
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(null,customerId);
        ResponseEntity<Object> response = restTemplate.exchange("/invoice/allowance", HttpMethod.GET,entity, Object.class);
        JtcResponse jtcResponse = getObject((Map) response.getBody(),JtcResponse.class);
        CurrentUsageAllowance currentUsageAllowance = getObject((Map) jtcResponse.getData(),CurrentUsageAllowance.class);
        assertThat(currentUsageAllowance).isEqualTo(exceptedCurrentUsageAllowance);
    }
    @Test
    public void ShouldGetCurrentUsageAllowanceZeroWhenExtra() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 20, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 20, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(2))));
        }};
        subscriptDao.createNewSubscript(new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), packageInfo));
        usageList.forEach(usageDetailsDao::addNewUsage);
        CurrentUsageAllowance exceptedCurrentUsageAllowance = new CurrentUsageAllowance(0,0);
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(null,customerId);
        ResponseEntity<Object> response = restTemplate.exchange("/invoice/allowance", HttpMethod.GET,entity, Object.class);
        JtcResponse jtcResponse = getObject((Map) response.getBody(),JtcResponse.class);
        CurrentUsageAllowance currentUsageAllowance = getObject((Map) jtcResponse.getData(),CurrentUsageAllowance.class);
        assertThat(currentUsageAllowance).isEqualTo(exceptedCurrentUsageAllowance);
    }

    @Test
    public void ShouldGetCurrentPay() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 6, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(1))));
            add(new Usage(customerId, 9, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 6, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(6))));
        }};
        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        Pay exceptedPay = new Pay(new BigDecimal(40));
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(null,customerId);
        ResponseEntity<Object> response = restTemplate.exchange("/invoice/currentPay", HttpMethod.GET,entity, Object.class);
        JtcResponse jtcResponse = getObject((Map) response.getBody(),JtcResponse.class);
        Pay pay = getObject((Map) jtcResponse.getData(),Pay.class);
        assertThat(pay).isEqualTo(exceptedPay);
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
