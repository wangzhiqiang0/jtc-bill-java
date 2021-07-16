package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.mock.BillingDaoImpl;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class BillingReadingControllerTest {

    private BillingReadingController billingReadingController;

    InvoiceDaoImpl invoiceDao;
    BillingDaoImpl billingDao;
    @BeforeEach
    public void setUp() {

        billingDao = new BillingDaoImpl();
        billingDao.setBillingList(new ArrayList<>());
        PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
        invoiceDao = new InvoiceDaoImpl();
        PackageReadingService packageReadingService = new PackageReadingService(packageReadingDao);
        InvoiceService invoiceService = new InvoiceService(invoiceDao);
        BillingService billingService = new BillingService(packageReadingService,invoiceService,billingDao);

        billingReadingController = new BillingReadingController(billingService, packageReadingService);

    }


    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackage() {
        assertThat(billingReadingController.subscriptPackage(TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(billingDao.getBillingList().stream().filter(e -> e.getCustomerId().equals(TestUtils.CUSTOMER_ID)).
                collect(Collectors.toList()).size()).isEqualTo(1);
    }
    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackageReturnErrorResponseWhenSubscriptDone() {
        billingDao.getBillingList().add(new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0)));
        assertThat(billingReadingController.subscriptPackage(TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Test
    public void givenCustomerIdSCurrentBillingPeriod() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(6);
        exceptedCurrentBillingAllowance.setPhoneAllowance(0);
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(14);
        billing.setPhoneUsed(21);
        billing.setSmsPay(10);
        billing.setPhonePay(10);
        billingDao.getBillingList().add(billing);
        assertThat(billingReadingController.currentBillingPeriod(TestUtils.CUSTOMER_ID).getBody()).isEqualTo(exceptedCurrentBillingAllowance);
    }

    @Test
    public void givenCustomerIdAndPhoneUsedPhone() {
        assertThat(billingReadingController.usedPhone(TestUtils.CUSTOMER_ID, 1).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void givenCustomerIdAndSMSUsedSMS() {

        assertThat(billingReadingController.usedSMS(TestUtils.CUSTOMER_ID, 1).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }


}
