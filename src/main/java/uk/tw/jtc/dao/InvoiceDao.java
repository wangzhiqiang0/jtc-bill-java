package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import uk.tw.jtc.model.Invoice;

import java.util.List;

@Mapper
public interface InvoiceDao {
    void createInvoice(Invoice invoice);
    void updateInvoice(String invoiceId,String status);

    List<Invoice> getActiveInvoice(String customerId);
}
