package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.response.Pay;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class BillingService {
    public List<Billing> getNeedToProceedCustomerList;
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
    public Billing subscriptPackage(String customerId, PackageInfo info) {
        Billing billing = new Billing(customerId, Instant.now(), info);
        billingDao.createNewBill(billing);

        return billing;
    }

    public CurrentBillingAllowance currentBillingPeriod(Billing billing) {
        CurrentBillingAllowance currentBillingAllowance = new CurrentBillingAllowance();
//        PaymentService paymentService = PaymentService.getPaymentService(billing);
//        int phoneExtra = paymentService.getExtraPhone();
//        if (phoneExtra < 0) {
//            currentBillingAllowance.setPhoneAllowance(-phoneExtra);
//        }
//        int smsExtra = paymentService.getExtraSms();
//        if (smsExtra < 0) {
//            currentBillingAllowance.setSmsAllowance(-smsExtra);
//        }
        return currentBillingAllowance;
    }




    public Pay getBillAtAnyTime(Billing billing) {
        Pay pay = new Pay();

       // pay.setPay(PaymentService.getPaymentService(billing).getCharge());

        return pay;
    }


    public Billing getBillByComerId(String customerId) {
        return billingDao.getBillByCustomerId(customerId);
    }

    public List<Billing> getNeedToProceedCustomerList() {
        return billingDao.getBillingList().stream().filter(e->e.getSubscriptTime().);
    }
}
