package uk.tw.jtc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.tw.jtc.TestApp;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.model.Usage;
import uk.tw.jtc.utils.TestUtils;
import uk.tw.jtc.utis.JtcTime;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
public class SubscriptServiceTest {
    @Autowired
    SubscriptDao subscriptDao;
    @Autowired
    SubscriptService subscriptService;

    @Test
    public void ShouldGetSubscriptListWhichInInvoiceDate() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        subscriptDao.createNewSubscript(new Subscript("customer1", Instant.parse("2021-06-17T15:00:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer2", Instant.parse("2021-07-16T14:00:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer3", Instant.parse("2021-07-16T16:01:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer4", Instant.parse("2021-08-16T14:00:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer5", Instant.parse("2021-08-16T16:01:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer6", Instant.parse("2021-08-20T14:00:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer7", Instant.parse("2021-09-16T14:00:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer8", Instant.parse("2021-09-16T17:00:00Z"), packageInfo));
        subscriptDao.createNewSubscript(new Subscript("customer9", Instant.parse("2021-09-17T00:00:00Z"), packageInfo));
        List<Subscript> subscripts = subscriptService.getNeedToProceedCustomerList(LocalDate.of(2021, 9, 17));
        // 16 16   17-16
        assertThat(subscripts).isEqualTo(Arrays.asList(
                new Subscript("customer1", Instant.parse("2021-06-17T15:00:00Z"), packageInfo),
                new Subscript("customer3", Instant.parse("2021-07-16T16:01:00Z"), packageInfo),
                new Subscript("customer5", Instant.parse("2021-08-16T16:01:00Z"), packageInfo)));
    }

}
