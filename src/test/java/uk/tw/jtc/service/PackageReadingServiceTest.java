package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.utils.TestUtils;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class PackageReadingServiceTest {

    private PackageReadingService packageReadingService;
    PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
    @BeforeEach
    public void setUp() {

        packageReadingDao.setPackageInfo(TestUtils.packageInfoList.get(0));
        packageReadingDao.setPackageInfoList(TestUtils.packageInfoList);
        this.packageReadingService = new PackageReadingService(packageReadingDao);
    }
    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingService.listPackages()).isEqualTo(TestUtils.packageInfoList);
    }

}
