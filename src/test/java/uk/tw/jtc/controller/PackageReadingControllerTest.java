package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PackageReadingControllerTest {

    private PackageReadingController packageReadingController;
    PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
    @BeforeEach
    public void setUp() {
        packageReadingDao.setPackageInfo(TestUtils.packageInfoList.get(0));
        packageReadingDao.setPackageInfoList(TestUtils.packageInfoList);
        packageReadingController = new PackageReadingController(new PackageReadingService(packageReadingDao));
    }


    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingController.listPackages().getBody()).isEqualTo(TestUtils.packageInfoList);
    }

    @Test
    public void listPackagesWhenNotFindPackageShouldReturnErrorResponse() {
        packageReadingDao.setPackageInfoList(null);
        assertThat(packageReadingController.listPackages().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}
