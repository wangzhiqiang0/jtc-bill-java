package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.CustomerMapperPackage;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        billing.setPhonePay(packageInfo.getPhoneLimit());
        billing.setSmsPay(packageInfo.getSmsLimit());
        billingDao.createNewBill(billing);

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

    private void createInvoice(Billing billing) {
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),billing.getCustomerId());
        invoice.setPay(billing.getSubscriptionFee());
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoiceService.createInvoice(invoice);
        billingDao.updatePhonePay(billing.getBillingId(),billing.getPhoneLimit());
        billingDao.updateSMSPay(billing.getBillingId(),billing.getSmsLimit());
    }
    private void updateInvoice(Billing billing) {
        Invoice invoice = invoiceService.getActiveInvoice(billing.getCustomerId());
        BigDecimal decimal = getInvoicePay(billing);
        if(invoice.getStatus().equals(PayEnum.PAID.getStatus()) && decimal.doubleValue()>0){

            invoice.setPay(decimal);
            invoice.setStatus(PayEnum.ACTIVE.getStatus());
            invoiceService.updateInvoice(invoice);
            if(billing.getPhoneUsed()>billing.getPhonePay()){
                billingDao.updatePhonePay(billing.getBillingId(),billing.getPhoneUsed());
            }
            if(billing.getSmsUsed()>billing.getSmsPay()){
                billingDao.updateSMSPay(billing.getBillingId(),billing.getSmsUsed());
            }

        }

    }

    public Invoice getBillAtAnyTime(String customerId){
        Billing billing = billingDao.getBillByCustomerId(customerId);
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),customerId);
        BigDecimal decimal = getInvoicePay(billing);

        invoice.setPay(decimal);
        return invoice;
    }

    private BigDecimal getInvoicePay(Billing billing ) {
        BigDecimal bigDecimal = new BigDecimal(0);
        if(billing.getPhoneUsed()>billing.getPhonePay()){
            bigDecimal =bigDecimal.add(billing.getExtraPhoneFee().multiply(new BigDecimal(billing.getPhoneUsed() - billing.getPhonePay())));
        }
        if(billing.getSmsUsed()>billing.getSmsPay()){
            bigDecimal = bigDecimal.add(billing.getExtraSMSFee().multiply(new BigDecimal(billing.getSmsUsed() - billing.getSmsPay())));
        }
        return bigDecimal;
    }

    @Transactional
    public String generateBill() {
        List<Billing> allBillingList = billingDao.getBillingList();
        List<Billing> newSubBillingList = allBillingList.stream().filter(e -> e.getSmsPay()==0 && e.getPhonePay()==0).
                collect(Collectors.toList());
        newSubBillingList.stream().forEach(e->createInvoice(e));
        List<Billing> needToPayBillingList = allBillingList.stream().
                filter(e-> (e.getSmsPay()!=0 && e.getPhonePay()!=0 &&
                        (e.getSubscriptTime().getDayOfMonth()) == (LocalDate.now().getDayOfMonth()-1))).collect(Collectors.toList());
        needToPayBillingList.stream().forEach(e->updateInvoice(e));

        return "success";

    }
}
