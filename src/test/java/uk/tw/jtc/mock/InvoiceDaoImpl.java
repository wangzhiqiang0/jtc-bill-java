package uk.tw.jtc.mock;

import lombok.Getter;
import lombok.Setter;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.model.Invoice;

import java.util.List;
import java.util.stream.Collectors;
@Setter
@Getter
public class InvoiceDaoImpl implements InvoiceDao {
    private List<Invoice> invoicesList;
    @Override
    public void createInvoice(Invoice invoice) {
        invoicesList.add(invoice);
    }

    @Override
    public void updateInvoice(String invoiceId, String status) {
        Invoice invoice = invoicesList.stream().filter(e -> e.getInvoiceId().equals(invoiceId)).findFirst().get();
        invoice.setStatus(status);
    }

    @Override
    public List<Invoice> getActiveInvoice(String customerId) {

        return invoicesList.stream().filter(e -> e.getCustomerId().equals(customerId)).collect(Collectors.toList());
    }
}
