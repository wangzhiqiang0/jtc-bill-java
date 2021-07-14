package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.service.PackageReadingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PackageReadingControllerTest {
    private static final String CUSTOMER_ID = "10101010";
    private PackageReadingController packageReadingController;
    private PackageReadingService packageReadingService;

    final List<PackageInfo> packageInfoList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        packageInfoList.add(new PackageInfo("Starter",new BigDecimal(38),
                10,10,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo("Standard",new BigDecimal(58),
                30,40,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo("Starter",new BigDecimal(188),
                300,200,new BigDecimal(1),new BigDecimal(0.5)));
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
        packageReadingService = new PackageReadingService(packageReadingDao);
        packageReadingController = new PackageReadingController(packageReadingService);
    }
    @Test
    public void givenCustomerIdGetSubscriptionPackageShouldReturnPackage() {
        assertThat(packageReadingController.getSubscriptionPackage(CUSTOMER_ID).getBody()).isEqualTo(packageInfoList.get(0));
    }

    @Test
    public void givenCustomerIdGetSubscriptionPackageShouldReturnShouldReturnErrorResponse() {
        PackageReadingService packageReadingServiceTemp = new PackageReadingService(new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return null;
            }

            @Override
            public List<PackageInfo> listPackages() {
                return null;
            }
        });
        packageReadingController = new PackageReadingController(packageReadingServiceTemp);
        assertThat(packageReadingController.getSubscriptionPackage(CUSTOMER_ID).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingController.listPackages().getBody()).isEqualTo(packageInfoList);
    }
}
