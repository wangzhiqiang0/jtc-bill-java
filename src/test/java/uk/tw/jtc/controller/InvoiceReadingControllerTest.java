package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InvoiceReadingControllerTest {

    private InvoiceReadingController invoiceReadingController;
    @BeforeEach
    public void setUp() {
        invoiceReadingController = new InvoiceReadingController(TestUtils.generateInvoiceService());
    }

    @Test
    public void givenCustomerIdGetActiveInvoice() {
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        assertThat(invoiceReadingController.getActiveInvoice(TestUtils.CUSTOMER_ID).getBody()).isEqualTo(invoice);
    }
}
