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
import uk.tw.jtc.dao.PaymentDao;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.*;
import uk.tw.jtc.request.PaymentRequest;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.response.CurrentBillingAllowance;
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
        subscriptDao.createNewSubscript(new Subscript(TestUtils.CUSTOMER_ID, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), TestUtils.packageInfoList.get(0)));
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
        subscriptDao.createNewSubscript(new Subscript(TestUtils.CUSTOMER_ID, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), TestUtils.packageInfoList.get(0)));
        Instant incurredAt = Instant.parse("2007-07-03T10:15:30.00Z");
        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(new UsageRequest(5, incurredAt));
        ResponseEntity<Object> response = restTemplate.postForEntity("/usage/sms", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Usage> usageList = usageDetailsDao.getUsageListByCustomerId(TestUtils.CUSTOMER_ID);
        Usage usage = usageList.get(0);
        assertThat(usage).isEqualTo(new Usage(TestUtils.CUSTOMER_ID, 5, UsageTypeEnum.SMS.getType(), incurredAt));

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

        ResponseEntity<Object> response = restTemplate.postForEntity("/subscript/subscriptPackage/"+packageInfo.getPackageId(), HttpRequestBuilder.getUsageHttpEntity(null), Object.class);
        Subscript subscript = new Subscript(TestUtils.CUSTOMER_ID, Instant.parse("2021-06-17T15:00:00Z"), packageInfo);
        Subscript subscript1 =subscriptDao.getSubscriptByCustomerId(TestUtils.CUSTOMER_ID);
        subscript1.setSubscriptTime(Instant.parse("2021-06-17T15:00:00Z"));
        assertThat(subscript).isEqualTo(subscript1);
        Invoice invoice = invoiceDao.getActiveInvoice(TestUtils.CUSTOMER_ID).stream().findFirst().get();
        assertThat(invoice).isEqualTo(new Invoice(TestUtils.CUSTOMER_ID, new BigDecimal(38), 0, 0, Instant.now()));

    }

    @Test
    public void shouldSavePaymentWhenCustomerPay() {
        Subscript subscript = new Subscript(TestUtils.CUSTOMER_ID, JtcTime.localDateToInstant(LocalDate.now().minusMonths(1)), TestUtils.packageInfoList.get(0));
        subscriptDao.createNewSubscript(subscript);
        Invoice invoice = new Invoice(subscript.getCustomerId(), subscript.getPackageInfo().getSubscriptionFee(), 0, 0, Instant.parse("2021-06-17T15:00:00Z"));
        invoiceDao.createNewInvoice(invoice);

        HttpEntity<String> entity = HttpRequestBuilder.getUsageHttpEntity(new PaymentRequest(invoice.getInvoiceId(), new BigDecimal(38)));
        ResponseEntity<Object> response = restTemplate.postForEntity("/invoice/paid", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Payment> payments = paymentDao.getSubscriptByCustomerId(TestUtils.CUSTOMER_ID);
        Payment payment = payments.get(0);
        Payment payment1 = new Payment(invoice.getInvoiceId(),TestUtils.CUSTOMER_ID, subscript.getPackageInfo().getSubscriptionFee());
        payment1.setPaymentId(payment.getPaymentId());
        payment1.setCreatedAt(payment.getCreatedAt());
        assertThat(payment).isEqualTo(payment1);
    }

}
