package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.request.RequestInvoice;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InvoiceReadingControllerTest {

    private InvoiceReadingController invoiceReadingController;

    InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    @BeforeEach
    public void setUp() {
        invoiceReadingController = new InvoiceReadingController(new InvoiceService(invoiceDao));
    }

    @Test
    public void givenCustomerIdGetActiveInvoice() {
        List<Invoice> invoiceList = new ArrayList<>();
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        invoiceList.add(invoice);
        invoiceDao.setInvoicesList(invoiceList);
        assertThat(invoiceReadingController.getActiveInvoice(TestUtils.CUSTOMER_ID).getBody()).isEqualTo(invoiceList);
    }

    @Test
    public void paidInvoiceShouldReturn() {
        List<Invoice> invoiceList = new ArrayList<>();
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        invoiceList.add(invoice);
        invoiceDao.setInvoicesList(invoiceList);
        RequestInvoice requestInvoice = new RequestInvoice();
        requestInvoice.setInvoiceId(invoice.getInvoiceId());
        requestInvoice.setPay(invoice.getPay());
        assertThat(invoiceReadingController.paidInvoice(TestUtils.CUSTOMER_ID,requestInvoice).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void paidInvoiceShouldReturnErrorResponse() {
        List<Invoice> invoiceList = new ArrayList<>();
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        invoiceList.add(invoice);
        invoiceDao.setInvoicesList(invoiceList);

        RequestInvoice requestInvoice = new RequestInvoice();
        requestInvoice.setInvoiceId(UUID.randomUUID().toString());
        requestInvoice.setPay(invoice.getPay());
        assertThat(invoiceReadingController.paidInvoice(TestUtils.CUSTOMER_ID,requestInvoice).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
