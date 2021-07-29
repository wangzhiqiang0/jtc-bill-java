package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.mock.BillingDaoImpl;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.utils.TestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsageDetailsServiceTest {
    UsageDetailsService usageDetailsService;
    UsageDetailsDao usageDetailsDao;

    @BeforeEach
    public void setUp() {
        BillingDaoImpl billingDao = new BillingDaoImpl();
        billingDao.setBillingList(new ArrayList<>());
        PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
        InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
        invoiceDao.setInvoicesList(new ArrayList<>());
        PackageReadingService packageReadingService = new PackageReadingService(packageReadingDao);
        InvoiceService invoiceService = new InvoiceService(invoiceDao);
        BillingService billingService = new BillingService(packageReadingService, invoiceService, billingDao);
        usageDetailsService = new UsageDetailsService(usageDetailsDao, billingService, invoiceService);
    }

    @Test
    public void shouldGiveLocalDateThenReturnInstant() {
        assertThat(usageDetailsService.
                localDateToInstant(LocalDate.of(2021, 9, 17))).isEqualTo(Instant.parse("2021-09-16T16:00:00Z"));

    }

    @Test
    public void shouldGiveLocalDateThenReturnInstant2() {
        assertThat(LocalDate.of(2021, 9, 17).minusMonths(1)).isEqualTo(LocalDate.of(2021, 8, 17));

    }
}
