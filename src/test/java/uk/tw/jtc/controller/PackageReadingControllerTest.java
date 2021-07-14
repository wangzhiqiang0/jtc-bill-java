package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PackageReadingControllerTest {

    private PackageReadingController packageReadingController;

    @BeforeEach
    public void setUp() {


        packageReadingController = new PackageReadingController(TestUtils.generatePackageReadingService());
    }
    @Test
    public void givenCustomerIdGetSubscriptionPackageShouldReturnPackage() {
        PackageReadingController packageReadingControllerTemp = new PackageReadingController(new PackageReadingService(new PackageReadingDao() {
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
        }));
        assertThat(packageReadingControllerTemp.getSubscriptionPackage(TestUtils.CUSTOMER_ID).getBody()).isEqualTo(TestUtils.packageInfoList.get(0));
    }

    @Test
    public void givenCustomerIdGetSubscriptionPackageShouldReturnShouldReturnErrorResponse() {

        packageReadingController = new PackageReadingController(TestUtils.generatePackageReadingService());
        assertThat(packageReadingController.getSubscriptionPackage(TestUtils.CUSTOMER_ID).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingController.listPackages().getBody()).isEqualTo(TestUtils.packageInfoList);
    }


}
