package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BillingService {
    private PackageReadingService packageReadingService;
    private InvoiceService invoiceService;

    private CustomerMapperPackageService customerMapperPackageService;

    private BillingDao billingDao;
    public BillingService(PackageReadingService packageReadingService, InvoiceService invoiceService,
                          CustomerMapperPackageService customerMapperPackageService,BillingDao billingDao) {
        this.packageReadingService = packageReadingService;
        this.invoiceService = invoiceService;
        this.customerMapperPackageService = customerMapperPackageService;
        this.billingDao = billingDao;
    }

    @Transactional
    public String subscriptPackage(String customerId, String packageId) {
        PackageInfo packageInfo = packageReadingService.getPackageById(packageId);
        customerMapperPackageService.subscriptPackage(customerId,packageInfo);
        Billing billing = new Billing(UUID.randomUUID().toString(),customerId,packageId);
        billingDao.createNewBill(billing);
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),customerId);
        invoice.setPay(packageInfo.getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setLastUpdateTime(LocalDate.now());
        invoiceService.createInvoice(invoice);
        return "success";
    }
    public CurrentBillingAllowance currentBillingPeriod(String customerId) {
        Billing billing = billingDao.getBillByCustomerId(customerId);
        PackageInfo packageInfo = packageReadingService.getPackageById(billing.getPackageId());
        CurrentBillingAllowance currentBillingAllowance = new CurrentBillingAllowance();
        if(billing.getPhoneUsed()<packageInfo.getPhoneLimit()){
            currentBillingAllowance.setPhoneAllowance(packageInfo.getPhoneLimit()-billing.getPhoneUsed());
        }
        if(billing.getSmsUsed()<packageInfo.getSmsLimit()){
            currentBillingAllowance.setSmsAllowance(packageInfo.getSmsLimit()-billing.getSmsUsed());
        }
        return currentBillingAllowance;
    }
    public String usedPhone(String customerId,int minute) {
        Billing billing = billingDao.getBillByCustomerId(customerId);
        billingDao.updatePhoneUsed(billing.getBillingId(),billing.getPhoneUsed()+minute);
        return "success";
    }
    public String usedSMS(String customerId,int num) {
        Billing billing = billingDao.getBillByCustomerId(customerId);
        billingDao.updateSMSUsed(billing.getBillingId(),billing.getSmsUsed()+num);
        return "success";
    }

}
