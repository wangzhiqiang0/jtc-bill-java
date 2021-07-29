package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.response.Pay;
import uk.tw.jtc.utis.JtcTime;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptService {
    private PackageReadingService packageReadingService;


    private SubscriptDao subscriptDao;

    public SubscriptService(PackageReadingService packageReadingService,
                            SubscriptDao subscriptDao) {
        this.packageReadingService = packageReadingService;
        this.subscriptDao = subscriptDao;
    }

    @Transactional
    public Subscript subscriptPackage(String customerId, PackageInfo info) {
        Subscript billing = new Subscript(customerId, Instant.now(), info);
        subscriptDao.createNewSubscript(billing);

        return billing;
    }

    public CurrentBillingAllowance currentBillingPeriod(Subscript billing) {
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




    public Pay getBillAtAnyTime(Subscript billing) {
        Pay pay = new Pay();

       // pay.setPay(PaymentService.getPaymentService(billing).getCharge());

        return pay;
    }


    public Subscript getBillByComerId(String customerId) {
        return subscriptDao.getSubscriptByCustomerId(customerId);
    }

    public List<Subscript> getNeedToProceedCustomerList(LocalDate invoiceDate) {
        return subscriptDao.getSubscriptList().stream().filter(e -> isInvoiceDate(e.getSubscriptTime(),invoiceDate)).collect(Collectors.toList());
    }

    public boolean isInvoiceDate(Instant subscriptTime,LocalDate invoiceDate) {
        return JtcTime.instantToLocalDate(subscriptTime).getDayOfMonth() == invoiceDate.getDayOfMonth()
                && JtcTime.instantToLocalDate(subscriptTime).compareTo(invoiceDate) != 0;
    }
}
