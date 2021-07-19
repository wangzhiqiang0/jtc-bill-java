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
        int needToPayPhone = billing.getPhoneUsed()-billing.getPhonePay();
        if(needToPayPhone<billing.getPackageInfo().getPhoneLimit()){
            currentBillingAllowance.setPhoneAllowance(billing.getPackageInfo().getPhoneLimit()-needToPayPhone);
        }
        int needToPaySms = billing.getSmsUsed() - billing.getSmsPay();
        if(needToPaySms < billing.getPackageInfo().getSmsLimit()){
            currentBillingAllowance.setSmsAllowance(billing.getPackageInfo().getSmsLimit()-needToPaySms);
        }
        return currentBillingAllowance;
    }

    @Transactional
    public List<Invoice> generateBill() {
        List<Invoice> invoiceList = new ArrayList<>();
        List<Billing> allBillingList = billingDao.getBillingList();

        List<Billing> newSubBillingList = allBillingList.stream().filter(e -> e.isFirst()).
                collect(Collectors.toList());
        List<Billing> needToPayBillingList = allBillingList.stream().filter(e -> !e.isFirst() &&
                e.getSubscriptTime().getDayOfMonth() == LocalDate.now().getDayOfMonth()).collect(Collectors.toList());
        newSubBillingList.forEach(e->{
            billingDao.updateFistToFalse(e.getBillingId());
            invoiceList.add(generateInvoice(getNewSubFunction(),e));
        });

        needToPayBillingList.forEach(e->{
            invoiceList.add(generateInvoice(getNoFirstFunction(),e));
        });
        if(invoiceList.size() !=0){
            invoiceService.createInvoice(invoiceList);
        }


        return invoiceList;

    }

    private Function<Billing, BigDecimal> getNoFirstFunction() {
        Function<Billing,BigDecimal> noFirstFunction = x -> {
            BigDecimal result = x.getPackageInfo().getSubscriptionFee();
            int needToPayPhone = x.getPhoneUsed()-x.getPhonePay();
            if(needToPayPhone > x.getPackageInfo().getPhoneLimit()){
                result =  result.add(x.getPackageInfo().getExtraPhoneFee().multiply(new BigDecimal(needToPayPhone -x.getPackageInfo().getPhoneLimit() )));
            }
            billingDao.updatePhonePay(x.getBillingId(),x.getPhoneUsed());
            int needToPaySms = x.getSmsUsed() - x.getSmsPay();
            if(needToPaySms > x.getPackageInfo().getSmsLimit()){
                result =  result.add(x.getPackageInfo().getExtraSMSFee().multiply(new BigDecimal(needToPaySms -x.getPackageInfo().getSmsLimit() )));
            }
            billingDao.updateSMSPay(x.getBillingId(),x.getSmsUsed());

            return result;
        };
        return noFirstFunction;
    }

    private Function<Billing, BigDecimal> getNoFirstFunction(boolean flag) {
        Function<Billing,BigDecimal> noFirstFunction = x -> {
            BigDecimal result = x.getPackageInfo().getSubscriptionFee();
            int needToPayPhone = x.getPhoneUsed()-x.getPhonePay();
            if(needToPayPhone > x.getPackageInfo().getPhoneLimit()){
                result =  result.add(x.getPackageInfo().getExtraPhoneFee().multiply(new BigDecimal(needToPayPhone -x.getPackageInfo().getPhoneLimit() )));
            }

            int needToPaySms = x.getSmsUsed() - x.getSmsPay();
            if(needToPaySms > x.getPackageInfo().getSmsLimit()){
                result =  result.add(x.getPackageInfo().getExtraSMSFee().multiply(new BigDecimal(needToPaySms -x.getPackageInfo().getSmsLimit() )));
            }
            return result;
        };
        return noFirstFunction;
    }

    private Function<Billing, BigDecimal> getNewSubFunction() {
        return x -> x.getPackageInfo().getSubscriptionFee();
    }

    public Invoice generateInvoice(Function<Billing,BigDecimal> function, Billing billing){
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),billing.getCustomerId());
        invoice.setPay(function.apply(billing));
        invoice.setStatus(PayEnum.ACTIVE.getStatus());
        invoice.setCreateTime(LocalDate.now());
        invoice.setLastUpdateTime(LocalDate.now());
        return invoice;

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

            pay.setPay(getNoFirstFunction(false).apply(billing));
        }

        return pay;
    }




    public Billing getBillByComerId(String customerId) {
        return billingDao.getBillByCustomerId(customerId);
    }
}
