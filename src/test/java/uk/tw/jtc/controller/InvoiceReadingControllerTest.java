package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.mock.PaymentDaoImpl;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.request.PaymentRequest;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PaymentService;
import uk.tw.jtc.utils.TestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InvoiceReadingControllerTest {

    private InvoiceReadingController invoiceReadingController;

    InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    @BeforeEach
    public void setUp() {
        PaymentService paymentService = new PaymentService(new PaymentDaoImpl());
        invoiceReadingController = new InvoiceReadingController(new InvoiceService(invoiceDao,
                null,null,paymentService),paymentService);
    }


    @Test
    public void paidInvoiceShouldReturnErrorResponseWhenNotFoundInvoice() {
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceDao.setInvoicesList(invoiceList);
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setInvoiceId(UUID.randomUUID().toString());
        paymentRequest.setPay(new BigDecimal(38));
       assertThat(invoiceReadingController.paidPayment(TestUtils.CUSTOMER_ID, paymentRequest).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void paidInvoiceShouldReturnErrorResponseWhenPayNotMatch() {
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceDao.setInvoicesList(invoiceList);
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setInvoiceId(UUID.randomUUID().toString());
        paymentRequest.setPay(new BigDecimal(38));
        Invoice invoice = new Invoice(TestUtils.CUSTOMER_ID,new BigDecimal(32),4,2, Instant.now());
        invoiceList.add(invoice);
        paymentRequest.setInvoiceId(invoice.getInvoiceId());
        assertThat(invoiceReadingController.paidPayment(TestUtils.CUSTOMER_ID, paymentRequest).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
