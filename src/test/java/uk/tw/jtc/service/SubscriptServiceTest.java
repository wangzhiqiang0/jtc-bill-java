package uk.tw.jtc.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.mock.SubscriptDaoImpl;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.utils.TestUtils;


import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SubscriptServiceTest {
    private SubscriptService subscriptService;
    private SubscriptDaoImpl subscriptDao;

    @BeforeEach
    public void setUp() {
        subscriptDao = new SubscriptDaoImpl();
        subscriptDao.setSubscriptList(new ArrayList<>());
        PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
        PackageReadingService packageReadingService = new PackageReadingService(packageReadingDao);
        subscriptService = new SubscriptService(packageReadingService, subscriptDao);

    }

    @Test
    public void givenInvoiceDateThenReturnIsInInvoice() {
        // 16 16   17-16
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-06-17T15:00:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(true);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-07-16T14:00:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(false);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-07-16T16:01:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(true);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-08-16T14:00:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(false);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-08-16T16:01:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(true);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-08-20T14:00:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(false);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-09-16T14:00:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(false);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-09-16T17:00:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(false);
        Assertions.assertThat(subscriptService.isInvoiceDate(Instant.parse("2021-09-17T00:00:00Z"), LocalDate.of(2021, 9, 17))).isEqualTo(false);
    }


}
