package uk.tw.jtc.service;

import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import uk.tw.jtc.dao.UsageDetailsDao;

import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Usage;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.utis.JtcTime;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class UsageDetailsService {
    private UsageDetailsDao usageDetailsDao;

    private BillingService billingService;
    private InvoiceService invoiceService;

    public UsageDetailsService(UsageDetailsDao usageDetailsDao, BillingService billingService, InvoiceService invoiceService) {
        this.usageDetailsDao = usageDetailsDao;
        this.billingService = billingService;
        this.invoiceService = invoiceService;
    }

    public Usage usage(String customerId, UsageRequest usageRequest, UsageTypeEnum usageTypeEnum) {
        Usage usage = new Usage(customerId, usageRequest.getUsage(), usageTypeEnum.getType(), usageRequest.getIncurredAt());
        usageDetailsDao.addNewUsage(usage);
        return usage;
    }

    public void generateInvoice() {
        generateInvoice(LocalDate.now());
    }

    public Instant localDateToInstant(LocalDate date) {
        return date.atStartOfDay().toInstant(ZoneOffset.ofHours(8));
    }

    public Map<String, Long> getUsageByAccountForLastMonth(LocalDate invoiceDate, String customer) {
        return usageDetailsDao.getCurrentCycleUsageListByCustomerId(customer, localDateToInstant(invoiceDate.minusMonths(1)), localDateToInstant(invoiceDate)).stream()
                .collect(Collectors.groupingBy(Usage::getType))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().collect(Collectors.summarizingInt(Usage::getUsage)).getSum())
                );
    }

    public void generateInvoice(LocalDate invoiceDate) {
        List<Billing> needToProceedCustomerList = billingService.getNeedToProceedCustomerList();

        needToProceedCustomerList.forEach(e -> {
            // List<Usage> usageList = getUsageByAccountForLastMonth(invoiceDate, e.getCustomerId());
        });
        List<Usage> usagesList = usageDetailsDao.getCurrentCycleUsageList();
//        Map<String,List<Usage>> customerMap = usagesList.stream().collect(Collectors.groupingBy(Usage::getCustomerId));
//
//        List<Invoice> invoiceList = customerMap.keySet().stream().map(e->{
//            List<Usage> currentCustomerList = customerMap.get(e);
//            Map<String,List<Usage>> typeMap = currentCustomerList.stream().collect(Collectors.groupingBy(Usage::getType));
//            int phoneUsage = (int) typeMap.get(UsageTypeEnum.PHONE.getType()).stream().collect(Collectors.summarizingInt(Usage::getUsage)).getSum();
//            int smsUsage = (int) typeMap.get(UsageTypeEnum.SMS.getType()).stream().collect(Collectors.summarizingInt(Usage::getUsage)).getSum();
//            PackageInfo packageInfo = billingService.getBillByComerId(e).getPackageInfo();
//            int extraPhone = Math.max(phoneUsage - packageInfo.getPhoneLimit(), 0);
//            int extraSms = smsUsage - packageInfo.getSmsLimit() > 0 ? smsUsage - packageInfo.getSmsLimit() : 0;
//            BigDecimal bigDecimal = packageInfo.getSubscriptionFee()
//                    .add(new BigDecimal(extraPhone * packageInfo.getExtraPhoneFee().doubleValue()+extraSms * packageInfo.getExtraSMSFee().doubleValue()));
//            return new Invoice(e, bigDecimal, phoneUsage, smsUsage, Instant.now());
//        });
//        if(!invoiceList.isEmpty()){
//            invoiceService.createInvoice(invoiceList);
//        }

    }
}
