package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uk.tw.jtc.model.Invoice;

import java.util.List;

@Mapper
public interface InvoiceDao {
    void createInvoice(@Param("invoices") List<Invoice> invoices);
    void createNewInvoice(@Param("invoice") Invoice invoice);
    List<Invoice> getActiveInvoice(@Param("customerId")String customerId);
    Invoice getInvoiceByInvoiceIdAndCustomerId(@Param("customerId")String customerId,@Param("invoiceId") String invoiceId);
}
