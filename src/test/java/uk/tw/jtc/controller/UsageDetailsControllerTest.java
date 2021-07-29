package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.utils.TestUtils;

public class UsageDetailsControllerTest {
    private UsageDetailsController usageDetailsController;
    PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
    @BeforeEach
    public void setUp() {
        packageReadingDao.setPackageInfoList(TestUtils.packageInfoList);
       // packageReadingController = new UsageDetailsController(new PackageReadingService(packageReadingDao));
    }
}
