package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class BillingReadingControllerTest {
    private static final String CUSTOMER_ID = "10101010";
    private BillingReadingController billingReadingController;


    List<PackageInfo> packageInfoList;

    @BeforeEach
    public void setUp() {
        packageInfoList = TestUtils.generatePackageInfoList();
        BillingService billingService = TestUtils.generateBillingService();
        PackageReadingService packageReadingService=TestUtils.generatePackageReadingService();

        billingReadingController = new BillingReadingController(billingService,packageReadingService);

    }


    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackage() {
        assertThat(billingReadingController.subscriptPackage(CUSTOMER_ID,packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackageReturnErrorResponseWhenSubscriptDone() {
        PackageReadingDao packageReadingDao = new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return packageInfoList.get(0);
            }

            @Override
            public List<PackageInfo> listPackages() {
                return packageInfoList;
            }
        };
        BillingReadingController billingReadingControllerTemp = new BillingReadingController
                (TestUtils.generateBillingService(),new PackageReadingService(packageReadingDao));
        assertThat(billingReadingControllerTemp.subscriptPackage(CUSTOMER_ID,packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
