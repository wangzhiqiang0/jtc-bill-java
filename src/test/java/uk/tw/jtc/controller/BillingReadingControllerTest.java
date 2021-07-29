package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.mock.BillingDaoImpl;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.util.ArrayList;
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
        packageReadingDao.setPackageInfoList(TestUtils.packageInfoList);
        invoiceDao = new InvoiceDaoImpl();
        PackageReadingService packageReadingService = new PackageReadingService(packageReadingDao);
        InvoiceService invoiceService = new InvoiceService(invoiceDao);
        BillingService billingService = new BillingService(packageReadingService, invoiceService, billingDao);

        billingReadingController = new BillingReadingController(billingService, packageReadingService);

    }


    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackage() {
        assertThat(billingReadingController.subscriptPackage(TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(billingDao.getBillingList().stream().filter(e -> e.getCustomerId().equals(TestUtils.CUSTOMER_ID)).
                collect(Collectors.toList()).size()).isEqualTo(1);
    }

//    @Test
//    public void givenCustomerIdAndPackageIdSubscriptPackageReturnErrorResponseWhenSubscriptDone() {
//        billingDao.getBillingList().add(new Billing(UUID.randomUUID().toString(), TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0)));
//        assertThat(billingReadingController.subscriptPackage(TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    }


//    @Test
//    public void givenCustomerIdSCurrentBillingPeriod() {
//        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
//        exceptedCurrentBillingAllowance.setSmsAllowance(6);
//        exceptedCurrentBillingAllowance.setPhoneAllowance(0);Billing billing = new Billing(UUID.randomUUID().toString(), TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0));
//
//        billingDao.getBillingList().add(billing);
//        JtcResponse JtcResponse = (JtcResponse) billingReadingController.currentBillingPeriod(TestUtils.CUSTOMER_ID).getBody();
//        assertThat(JtcResponse.getData()).isEqualTo(exceptedCurrentBillingAllowance);
//    }

    @Test
    public void execute() {
     //   assertThat(billingReadingController.execute().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
