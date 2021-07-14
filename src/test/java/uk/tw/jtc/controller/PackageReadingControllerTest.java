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
    private static final String CUSTOMER_ID = "10101010";
    private PackageReadingController packageReadingController;
    private PackageReadingService packageReadingService;

    List<PackageInfo> packageInfoList;

    @BeforeEach
    public void setUp() {
        packageInfoList = TestUtils.generatePackageInfoList();
        PackageReadingDao packageReadingDao = new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return null;
            }

            @Override
            public List<PackageInfo> listPackages() {
                return packageInfoList;
            }
        };
        packageReadingService = new PackageReadingService(packageReadingDao);
        packageReadingController = new PackageReadingController(packageReadingService);
    }
    @Test
    public void givenCustomerIdGetSubscriptionPackageShouldReturnPackage() {
        PackageReadingController packageReadingControllerTemp = new PackageReadingController(new PackageReadingService(new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return packageInfoList.get(0);
            }

            @Override
            public List<PackageInfo> listPackages() {
                return packageInfoList;
            }
        }));
        assertThat(packageReadingControllerTemp.getSubscriptionPackage(CUSTOMER_ID).getBody()).isEqualTo(packageInfoList.get(0));
    }

    @Test
    public void givenCustomerIdGetSubscriptionPackageShouldReturnShouldReturnErrorResponse() {

        packageReadingController = new PackageReadingController(packageReadingService);
        assertThat(packageReadingController.getSubscriptionPackage(CUSTOMER_ID).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingController.listPackages().getBody()).isEqualTo(packageInfoList);
    }


}
