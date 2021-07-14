package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.utils.TestUtils;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InvoiceServiceTest {
    private InvoiceService invoiceService;

    @BeforeEach
    public void setUp() {
        invoiceService = TestUtils.generateInvoiceService();
    }

    @Test
    public void givenCustomerIdGetActiveInvoice() {
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        assertThat(invoiceService.getActiveInvoice(TestUtils.CUSTOMER_ID)).isEqualTo(invoice);
    }
}
