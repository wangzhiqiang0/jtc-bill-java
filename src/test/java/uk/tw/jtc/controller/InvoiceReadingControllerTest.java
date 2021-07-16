package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PackageReadingService;
import uk.tw.jtc.utils.TestUtils;

import java.math.BigDecimal;
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

    @Test
    public void givenCustomerIdGetActiveInvoiceShouldReturnErrorResponse() {
//        InvoiceService invoiceService= new InvoiceService(new InvoiceDao() {
//            @Override
//            public void createInvoice(Invoice invoice) {
//
//            }
//
//            @Override
//            public void updateInvoice(Invoice invoice) {
//
//            }
//
//            @Override
//            public Invoice getActiveInvoice(String customerId) {
//                return null;
//            }
//        });
        InvoiceReadingController temp = new InvoiceReadingController(null);

        assertThat(temp.getActiveInvoice(TestUtils.CUSTOMER_ID).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void paidInvoiceShouldReturn() {
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        assertThat(invoiceReadingController.paidInvoice(TestUtils.CUSTOMER_ID,invoice).getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void paidInvoiceShouldReturnErrorResponse() {
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        invoice.setPay(new BigDecimal(20));
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        assertThat(invoiceReadingController.paidInvoice(TestUtils.CUSTOMER_ID,invoice).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
