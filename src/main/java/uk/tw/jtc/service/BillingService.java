package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;

@Service
public class BillingService {
    private PackageReadingService packageReadingService;
    private InvoiceService invoiceService;

    private BillingDao billingDao;
    public BillingService(PackageReadingService packageReadingService, InvoiceService invoiceService,BillingDao billingDao) {
        this.packageReadingService = packageReadingService;
        this.invoiceService = invoiceService;
        this.billingDao = billingDao;
    }

    @Transactional
    public String subscriptPackage(String customerId, String packageId) {
        packageReadingService.subscriptPackage(customerId,packageId);
        Billing billing = new Billing();
        Invoice invoice = new Invoice();
        billingDao.createNewBill(billing);
        invoiceService.createInvoice(invoice);
        return "success";
    }
}
