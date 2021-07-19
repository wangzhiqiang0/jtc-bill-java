package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.model.Invoice;

import java.util.List;

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
        invoiceDao.updateInvoice(invoice.getInvoiceId(),invoice.getStatus());
    }

    public List<Invoice> getActiveInvoice(String customerId) {
        return invoiceDao.getActiveInvoice(customerId);
    }

}
