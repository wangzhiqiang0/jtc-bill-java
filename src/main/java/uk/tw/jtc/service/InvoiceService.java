package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.Usage;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private InvoiceDao invoiceDao;
    private UsageDetailsService usageDetailsService;
    private SubscriptService subscriptService;
    private PaymentService paymentService;

    public InvoiceService(InvoiceDao invoiceDao, UsageDetailsService usageDetailsService, SubscriptService subscriptService,PaymentService paymentService) {
        this.invoiceDao = invoiceDao;
        this.subscriptService = subscriptService;
        this.usageDetailsService = usageDetailsService;
        this.paymentService = paymentService;
    }



    public List<Invoice> getActiveInvoice(String customerId) {
        return invoiceDao.getActiveInvoice(customerId);
    }

    public Invoice getInvoiceByInvoiceIdAndCustomerId(String customerId,String invoiceId){
        return invoiceDao.getInvoiceByInvoiceIdAndCustomerId(customerId,invoiceId);
    }


    public void generateInvoice() {
        generateInvoice(LocalDate.now());
    }

    public List<Invoice> generateInvoice(LocalDate invoiceDate) {
        List<Invoice> invoices= subscriptService.getNeedToProceedCustomerList(invoiceDate).stream().
                map(e -> generateInvoiceFunction().apply(e, usageDetailsService.getUsageByAccountForLastMonth(invoiceDate,e.getCustomerId())))
                .collect(Collectors.toList());
        if(!invoices.isEmpty()){
            invoiceDao.createInvoice(invoices);
        }
        return invoices;
    }

    public BiFunction<Subscript, Map<String, Long>, Invoice> generateInvoiceFunction() {
        return (sub, map) -> new Invoice(sub.getCustomerId(), getTotalInvoicePay(sub, map), map.get(UsageTypeEnum.PHONE.getType()).intValue(), map.get(UsageTypeEnum.SMS.getType()).intValue(), Instant.now());
    }

    public BigDecimal getTotalInvoicePay(Subscript sub, Map<String, Long> map) {
        return sub.getPackageInfo().getSubscriptionFee()
                .add(new BigDecimal(
                        Math.max(map.get(UsageTypeEnum.PHONE.getType()).intValue() - sub.getPackageInfo().getPhoneLimit(), 0) * sub.getPackageInfo().getExtraPhoneFee().doubleValue()
                                + Math.max(map.get(UsageTypeEnum.SMS.getType()).intValue() - sub.getPackageInfo().getPhoneLimit(), 0) * sub.getPackageInfo().getExtraSMSFee().doubleValue()));
    }

    public void createInvoice(String customerId, PackageInfo packageInfo) {
        invoiceDao.createNewInvoice(new Invoice(customerId, packageInfo.getSubscriptionFee(), 0, 0, Instant.now()));
    }
}
