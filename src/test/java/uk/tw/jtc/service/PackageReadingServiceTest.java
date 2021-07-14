package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.utils.TestUtils;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class PackageReadingServiceTest {

    private PackageReadingService packageReadingService;

    @BeforeEach
    public void setUp() {
        this.packageReadingService = TestUtils.generatePackageReadingService();
    }
    @Test
    public void givenCustomerIdShouldReturnPackage() {
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
        assertThat(new PackageReadingService(packageReadingDao).getPackageByCustomerID(TestUtils.CUSTOMER_ID)).isEqualTo(TestUtils.packageInfoList.get(0));
    }
    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingService.listPackages()).isEqualTo(TestUtils.packageInfoList);
    }
}
