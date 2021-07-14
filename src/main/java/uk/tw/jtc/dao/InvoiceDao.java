package uk.tw.jtc.dao;

import uk.tw.jtc.model.Invoice;

public interface InvoiceDao {
    void createInvoice(Invoice invoice);
    void updateInvoice(Invoice invoice);

    Invoice getActiveInvoice(String customerId);
}
