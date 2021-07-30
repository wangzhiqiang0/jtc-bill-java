package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.mock.SubscriptDaoImpl;
import uk.tw.jtc.mock.UsageDetailsDaoImpl;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.service.SubscriptService;
import uk.tw.jtc.service.UsageDetailsService;
import uk.tw.jtc.utils.TestUtils;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsageDetailsControllerTest {
    private UsageDetailsController usageDetailsController;
    UsageDetailsDaoImpl usageDetailsDao = new UsageDetailsDaoImpl();
    SubscriptDaoImpl subscriptDao;
    @BeforeEach
    public void setUp() {
         subscriptDao = new SubscriptDaoImpl();

        PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
        packageReadingDao.setPackageInfoList(TestUtils.packageInfoList);
        subscriptDao.setSubscriptList(new ArrayList<>());
        PackageReadingService packageReadingService = new PackageReadingService(packageReadingDao);
        usageDetailsDao.setUsageList(new ArrayList<>());
        usageDetailsController = new UsageDetailsController(new SubscriptService(packageReadingService,subscriptDao),new UsageDetailsService(usageDetailsDao));
    }
    @Test
    public void shouldGetErrorCallPhoneUsageWhenSendZeroUsage() {

        subscriptDao.getSubscriptList().add(new Subscript(TestUtils.CUSTOMER_ID,Instant.now(),TestUtils.packageInfoList.get(0)));
        assertThat(usageDetailsController.usagePhone(TestUtils.CUSTOMER_ID, new UsageRequest(0,Instant.now())).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void shouldGetErrorCallPhoneUsageWhenNotSubscript() {
        assertThat(usageDetailsController.usagePhone(TestUtils.CUSTOMER_ID, new UsageRequest(1,Instant.now())).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
    @Test
    public void shouldGetErrorCallSMSUsageWhenSendZeroUsage() {

        subscriptDao.getSubscriptList().add(new Subscript(TestUtils.CUSTOMER_ID,Instant.now(),TestUtils.packageInfoList.get(0)));
        assertThat(usageDetailsController.usageSMS(TestUtils.CUSTOMER_ID, new UsageRequest(0,Instant.now())).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void shouldGetErrorCallSMSUsageWhenNotSubscript() {
        assertThat(usageDetailsController.usageSMS(TestUtils.CUSTOMER_ID, new UsageRequest(1,Instant.now())).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}
