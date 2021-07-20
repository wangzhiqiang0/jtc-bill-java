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
import uk.tw.jtc.response.Pay;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class BillingService {
    private PackageReadingService packageReadingService;
    private InvoiceService invoiceService;



    private BillingDao billingDao;
    public BillingService(PackageReadingService packageReadingService, InvoiceService invoiceService,
                          BillingDao billingDao) {
        this.packageReadingService = packageReadingService;
        this.invoiceService = invoiceService;
        this.billingDao = billingDao;
    }

    @Transactional
    public Billing subscriptPackage(String customerId, String packageId) {
        PackageInfo info = packageReadingService.getPackageById(packageId);
        Billing billing = new Billing(UUID.randomUUID().toString(),customerId,info);
        billingDao.createNewBill(billing);
        return billing;
    }

    public CurrentBillingAllowance currentBillingPeriod(String customerId) {
        Billing billing = billingDao.getBillByCustomerId(customerId);
        CurrentBillingAllowance currentBillingAllowance = new CurrentBillingAllowance();
        int phoneExtra = billing.getPhoneUsed()-billing.getPhonePay()-billing.getPackageInfo().getPhoneLimit();
        if(phoneExtra < 0){
            currentBillingAllowance.setPhoneAllowance(-phoneExtra);
        }
        int smsExtra = billing.getSmsUsed() - billing.getSmsPay()-billing.getPackageInfo().getSmsLimit();
        if(smsExtra < 0){
            currentBillingAllowance.setSmsAllowance(-smsExtra);
        }
        return currentBillingAllowance;
    }

    @Transactional
    public List<Invoice> generateBill() {
        List<Invoice> invoiceList = new ArrayList<>();
        List<Billing> billingListList = billingDao.getBillingList();
        billingListList.forEach(e -> {
            if(e.isFirst()){
                billingDao.updateFistToFalse(e.getBillingId());
                invoiceList.add(invoiceService.generateInvoice(getNewSubFunction(),e));
            }else if(e.getSubscriptTime().getDayOfMonth() == LocalDate.now().getDayOfMonth()){
                invoiceList.add(invoiceService.generateInvoice(getNoFirstFunction(),e));
                billingDao.updateSMSPay(e.getBillingId(),e.getSmsUsed());
                billingDao.updatePhonePay(e.getBillingId(),e.getPhoneUsed());
            }
        });
        if(invoiceList.size() !=0){
            invoiceService.createInvoice(invoiceList);
        }
        return invoiceList;

    }

    private Function<Billing, BigDecimal> getNoFirstFunction() {
        Function<Billing,BigDecimal> noFirstFunction = x -> {
            BigDecimal result = x.getPackageInfo().getSubscriptionFee();
            return result.add(getExtraPhoneFee(x)).add(getExtraSMSFee(x));
        };
        return noFirstFunction;
    }

    private BigDecimal getExtraPhoneFee(Billing billing){
        int extra = billing.getPhoneUsed()-billing.getPhonePay()-billing.getPackageInfo().getPhoneLimit();
        return extra>0 ? billing.getPackageInfo().getExtraPhoneFee().multiply(new BigDecimal(extra ))
                :new BigDecimal(0);
    }

    private BigDecimal getExtraSMSFee(Billing billing){
        int extra = billing.getSmsUsed() - billing.getSmsPay()-billing.getPackageInfo().getSmsLimit();
        return extra>0 ? billing.getPackageInfo().getExtraSMSFee().multiply(new BigDecimal(extra ))
                :new BigDecimal(0);
    }


    private Function<Billing, BigDecimal> getNewSubFunction() {
        return x -> x.getPackageInfo().getSubscriptionFee();
    }



    public Billing usedPhone(String customerId,int minute) {
        Billing billing = billingDao.getBillByCustomerId(customerId);
        billingDao.updatePhoneUsed(billing.getBillingId(),billing.getPhoneUsed()+minute);

        return billing;
    }
    public Billing usedSMS(String customerId,int num) {
        Billing billing = billingDao.getBillByCustomerId(customerId);
        billingDao.updateSMSUsed(billing.getBillingId(),billing.getSmsUsed()+num);
        return billing;
    }



    public Pay getBillAtAnyTime(String customerId){
        Billing billing = billingDao.getBillByCustomerId(customerId);
        Pay pay = new Pay();
        if(billing.isFirst()){
            pay.setPay(getNewSubFunction().apply(billing));
        }else {

            pay.setPay(getNoFirstFunction().apply(billing));
        }
        return pay;
    }




    public Billing getBillByComerId(String customerId) {
        return billingDao.getBillByCustomerId(customerId);
    }
}
