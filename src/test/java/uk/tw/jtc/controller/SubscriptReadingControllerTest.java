package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.mock.SubscriptDaoImpl;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.service.SubscriptService;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SubscriptReadingControllerTest {

    private SubscriptReadingController subscriptReadingController;


    SubscriptDaoImpl subscriptDao;
    PackageReadingDaoImpl packageReadingDao;

    @BeforeEach
    public void setUp() {

        subscriptDao = new SubscriptDaoImpl();
        subscriptDao.setSubscriptList(new ArrayList<>());
        packageReadingDao = new PackageReadingDaoImpl();
        packageReadingDao.setPackageInfoList(TestUtils.packageInfoList);

        PackageReadingService packageReadingService = new PackageReadingService(packageReadingDao);

        subscriptReadingController = new SubscriptReadingController(new SubscriptService(packageReadingService,subscriptDao), packageReadingService,null);

    }


    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackageWhenNoPackage() {
        packageReadingDao.setPackageInfoList(new ArrayList<>());
        assertThat(subscriptReadingController.subscriptPackage(TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackageWhenHasSubscript() {

        subscriptDao.getSubscriptList().add(new Subscript(TestUtils.CUSTOMER_ID, Instant.now(),TestUtils.packageInfoList.get(0)));
        assertThat(subscriptReadingController.subscriptPackage(TestUtils.CUSTOMER_ID, TestUtils.packageInfoList.get(0).getPackageId()).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }


}
