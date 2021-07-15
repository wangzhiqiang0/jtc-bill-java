package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import uk.tw.jtc.model.Invoice;
@Mapper
public interface InvoiceDao {
    void createInvoice(Invoice invoice);
    void updateInvoice(Invoice invoice);

    Invoice getActiveInvoice(String customerId);
}
