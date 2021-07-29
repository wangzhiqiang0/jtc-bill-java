package uk.tw.jtc.mock;

import lombok.Getter;
import lombok.Setter;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.model.Invoice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Setter
@Getter
public class InvoiceDaoImpl implements InvoiceDao {
    private List<Invoice> invoicesList;
    @Override
    public void createInvoice(List<Invoice> invoice) {
        invoicesList.addAll(invoice);
    }


    @Override
    public List<Invoice> getActiveInvoice(String customerId) {

        return invoicesList.stream().filter(e -> e.getCustomerId().equals(customerId)).collect(Collectors.toList());
    }

    @Override
    public Invoice getInvoiceByInvoiceIdAndCustomerId(String customerId, String invoiceId) {
        Optional<Invoice> invoices = invoicesList.stream().filter(invoice -> invoice.getInvoiceId().equals(invoiceId) && invoice.getCustomerId().equals(customerId)).findFirst();
        if(!invoices.isPresent()){
            return null;
        }
        return invoices.get();
    }

    @Override
    public void createNewInvoice(Invoice invoice) {
        invoicesList.add(invoice);
    }
}
