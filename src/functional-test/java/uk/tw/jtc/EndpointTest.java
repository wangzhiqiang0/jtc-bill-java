package uk.tw.jtc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.request.Used;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.response.JwtResponse;
import uk.tw.jtc.response.Pay;
import uk.tw.jtc.utils.TestUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
public class EndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
    }



    @Test
    public void functionalTest() throws JsonProcessingException {
        ResponseEntity<Object> response = restTemplate.getForEntity("/package/listPackages", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JwtResponse jwtResponse = getObject((Map) response.getBody(),JwtResponse.class);
        List<PackageInfo> packageInfoList = getObjectList((List) jwtResponse.getData(), PackageInfo.class);
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

        getBillAtAnyTime(packageInfo,firstPhoneUsed,firstSMSUsed);

        int secondPhoneUsed = 15;
        int secondSMSUsed = 15;
        usedPhone(secondPhoneUsed);
        usedSMS(secondSMSUsed);

        currentBillingPeriod(packageInfo, firstPhoneUsed + secondPhoneUsed, firstSMSUsed + secondSMSUsed);
        getBillAtAnyTime(packageInfo,firstPhoneUsed + secondPhoneUsed, firstSMSUsed + secondSMSUsed);

        execute();
        List<Invoice> invoices1 = getActiveInvoice();
        assertThat(invoices1.size()).isEqualTo(2);
        Invoice secondInvoice = invoices1.stream().filter(e -> !e.getInvoiceId().equals(firstInvoice.getInvoiceId())).findFirst().get();
        assertThat(secondInvoice.getPay().doubleValue()).isEqualTo(getInvoicePay(packageInfo,firstPhoneUsed + secondPhoneUsed, firstSMSUsed + secondSMSUsed));

    }

    private List<Invoice> getActiveInvoice() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange("/invoice/active", HttpMethod.GET, entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JwtResponse jwtResponse = getObject((Map) response.getBody(),JwtResponse.class);
        List<Invoice> invoices = getObjectList((List<Map>) jwtResponse.getData(),Invoice.class);

       return invoices;


    }

    private void getBillAtAnyTime(PackageInfo packageInfo,int phoneUsed,int smsUsed) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange("/billing/getInvoiceAnyTime", HttpMethod.GET, entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JwtResponse<Pay> jwtResponse= getObject((Map) response.getBody(), JwtResponse.class);
        Pay pay = getObject((Map) jwtResponse.getData(),Pay.class);

        assertThat(pay.getPay().doubleValue()).isEqualTo(getInvoicePay(packageInfo,phoneUsed,smsUsed));
    }

    private double getInvoicePay(PackageInfo packageInfo, int phoneUsed, int smsUsed) {
        double phoneExcepted = phoneUsed < packageInfo.getPhoneLimit() ? 0 : packageInfo.getExtraPhoneFee().doubleValue()*(phoneUsed-packageInfo.getPhoneLimit());
        double smsExcepted = smsUsed < packageInfo.getSmsLimit() ? 0 : packageInfo.getExtraSMSFee().doubleValue()*(smsUsed-packageInfo.getPhoneLimit());
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
        Used used = new Used();
        used.setSmsUsed(smsUsed);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        HttpEntity<String> entity = new HttpEntity(used, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity("/billing/usedSMS", entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void usedPhone(int phoneUsed) {
        Used used = new Used();
        used.setPhoneUsed(phoneUsed);
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
        JwtResponse jwtResponse = getObject((Map) response.getBody(),JwtResponse.class);
        CurrentBillingAllowance allowance = getObject((Map) jwtResponse.getData(), CurrentBillingAllowance.class);
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
