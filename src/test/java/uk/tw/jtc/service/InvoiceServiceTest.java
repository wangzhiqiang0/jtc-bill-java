package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.utils.TestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InvoiceServiceTest {
    private InvoiceService invoiceService;
    InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    @BeforeEach
    public void setUp() {

        invoiceService = new InvoiceService(invoiceDao);
    }

    @Test
    public void givenCustomerIdGetActiveInvoice() {
        List<Invoice> invoices = new ArrayList<>();
        List<Invoice> exceptedInvoices = new ArrayList<>();
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
    //    invoice.setStatus(PayEnum.ACTIVE.getStatus());
     //   invoice.setLastUpdateTime(LocalDate.now());


        Invoice invoice1 = new Invoice(UUID.randomUUID().toString(),UUID.randomUUID().toString());
        invoice1.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
     //   invoice1.setStatus(PayEnum.ACTIVE.getStatus());
      //  invoice1.setLastUpdateTime(LocalDate.now());

        invoices.add(invoice);
        invoices.add(invoice1);
        exceptedInvoices.add(invoice);

        invoiceDao.setInvoicesList(invoices);
        assertThat(invoiceService.getActiveInvoice(TestUtils.CUSTOMER_ID)).isEqualTo(exceptedInvoices);
    }
}
