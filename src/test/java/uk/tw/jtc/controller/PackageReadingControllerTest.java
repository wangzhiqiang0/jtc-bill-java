package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.response.JwtResponse;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PackageReadingControllerTest {

    private PackageReadingController packageReadingController;
    PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
    @BeforeEach
    public void setUp() {
        packageReadingDao.setPackageInfoList(TestUtils.packageInfoList);
        packageReadingController = new PackageReadingController(new PackageReadingService(packageReadingDao));
    }


    @Test
    public void listPackagesShouldReturnPackageList() {
        JwtResponse jwtResponse = (JwtResponse) packageReadingController.listPackages().getBody();
        assertThat(jwtResponse.getData()).isEqualTo(TestUtils.packageInfoList);
    }

    @Test
    public void listPackagesWhenNotFindPackageShouldReturnErrorResponse() {
        packageReadingDao.setPackageInfoList(null);
        assertThat(packageReadingController.listPackages().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}
