package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.model.Invoice;

@Service
public class InvoiceService {
    private InvoiceDao invoiceDao;

    public InvoiceService(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public void createInvoice(Invoice invoice) {
        invoiceDao.createInvoice(invoice);
    }

}
