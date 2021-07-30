package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.model.PackageInfo;
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


    public Subscript getSubscriptByCustomerId(String customerId) {
        return subscriptDao.getSubscriptByCustomerId(customerId);
    }

    public List<Subscript> getNeedToProceedCustomerList(LocalDate invoiceDate) {
        return subscriptDao.getSubscriptList().stream().filter(e -> isInvoiceDate(e.getSubscriptTime(),invoiceDate)).collect(Collectors.toList());
    }

    public boolean isInvoiceDate(Instant subscriptTime,LocalDate invoiceDate) {
        return JtcTime.instantToLocalDate(subscriptTime).getDayOfMonth() == invoiceDate.getDayOfMonth()
                && JtcTime.instantToLocalDate(subscriptTime).compareTo(invoiceDate) != 0;
    }
    public LocalDate getInvoiceDate(String customerId) {
        return getInvoiceDate(LocalDate.now(),customerId);
    }

    public LocalDate getInvoiceDate(LocalDate invoiceDate, String customerId) {
        LocalDate subscript = JtcTime.instantToLocalDate(subscriptDao.getSubscriptByCustomerId(customerId).getSubscriptTime());
        if (invoiceDate.getDayOfMonth() > subscript.getDayOfMonth()) {
            if (invoiceDate.getMonthValue() == 12) {
                return LocalDate.of(invoiceDate.getYear() + 1, 1, subscript.getDayOfMonth());
            } else {
                return LocalDate.of(invoiceDate.getYear(), invoiceDate.getMonthValue() + 1, subscript.getDayOfMonth());
            }
        } else {
            return LocalDate.of(invoiceDate.getYear(), invoiceDate.getMonthValue(), subscript.getDayOfMonth());
        }
    }
}
