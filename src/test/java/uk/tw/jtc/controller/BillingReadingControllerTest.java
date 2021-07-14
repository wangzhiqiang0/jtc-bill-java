package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class BillingReadingControllerTest {

    private BillingReadingController billingReadingController;
    @BeforeEach
    public void setUp() {

        BillingService billingService = TestUtils.generateBillingService();
        PackageReadingService packageReadingService=TestUtils.generatePackageReadingService();

        billingReadingController = new BillingReadingController(billingService,packageReadingService);

    }


    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackage() {
        assertThat(billingReadingController.subscriptPackage(TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
    @Test
    public void givenCustomerIdSCurrentBillingPeriod() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(5);
        exceptedCurrentBillingAllowance.setPhoneAllowance(5);
        assertThat(billingReadingController.currentBillingPeriod(TestUtils.CUSTOMER_ID).getBody()).isEqualTo(exceptedCurrentBillingAllowance);
   }
    @Test
    public void givenCustomerIdAndPhoneUsedPhone() {
        assertThat(billingReadingController.usedPhone(TestUtils.CUSTOMER_ID,1).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void givenCustomerIdAndSMSUsedSMS() {

        assertThat(billingReadingController.usedSMS(TestUtils.CUSTOMER_ID,1).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackageReturnErrorResponseWhenSubscriptDone() {
        PackageReadingDao packageReadingDao = new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return TestUtils.packageInfoList.get(0);
            }

            @Override
            public List<PackageInfo> listPackages() {
                return TestUtils.packageInfoList;
            }
            @Override
            public PackageInfo getPackageById(String packageId) {
                return TestUtils.packageInfoList.stream().filter(e -> e.getPackageId().equals(packageId)).findFirst().get();
            }
        };
        BillingReadingController billingReadingControllerTemp = new BillingReadingController
                (TestUtils.generateBillingService(),new PackageReadingService(packageReadingDao));
        assertThat(billingReadingControllerTemp.subscriptPackage(TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
