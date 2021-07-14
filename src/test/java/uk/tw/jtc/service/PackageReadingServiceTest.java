package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.utils.TestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class PackageReadingServiceTest {

    private static final String CUSTOMER_ID = "10101010";
    private PackageReadingService packageReadingService;
    List<PackageInfo> packageInfoList;
    @BeforeEach
    public void setUp() {
        packageInfoList =TestUtils.generatePackageInfoList();

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
        this.packageReadingService = new PackageReadingService(packageReadingDao);
    }
    @Test
    public void givenCustomerIdShouldReturnPackage() {
        assertThat(packageReadingService.getPackageByCustomerID(CUSTOMER_ID)).isEqualTo(packageInfoList.get(0));
    }
    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingService.listPackages()).isEqualTo(packageInfoList);
    }
}
