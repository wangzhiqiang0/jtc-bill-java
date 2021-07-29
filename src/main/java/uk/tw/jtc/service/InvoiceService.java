package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class InvoiceService {
    private InvoiceDao invoiceDao;

    public InvoiceService(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public void createInvoice(List<Invoice> invoice) {
        invoiceDao.createInvoice(invoice);
    }

    public void updateInvoice(Invoice invoice) {
        //invoiceDao.updateInvoice(invoice.getInvoiceId(), invoice.getStatus());
    }

    public List<Invoice> getActiveInvoice(String customerId) {
        return invoiceDao.getActiveInvoice(customerId);
    }

    public Invoice generateInvoice(Billing billing,BigDecimal charge) {
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), billing.getCustomerId());
        invoice.setPay(charge);
       // invoice.setStatus(PayEnum.ACTIVE.getStatus());
     //   invoice.setCreateTime(LocalDate.now());
       // invoice.setLastUpdateTime(LocalDate.now());

        return invoice;

    }




}
