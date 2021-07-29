package uk.tw.jtc.service;


import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.Usage;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.utis.JtcTime;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UsageDetailsService {
    private UsageDetailsDao usageDetailsDao;


    public UsageDetailsService(UsageDetailsDao usageDetailsDao) {
        this.usageDetailsDao = usageDetailsDao;
    }

    public Usage usage(String customerId, UsageRequest usageRequest, UsageTypeEnum usageTypeEnum) {
        Usage usage = new Usage(customerId, usageRequest.getUsage(), usageTypeEnum.getType(), usageRequest.getIncurredAt());
        usageDetailsDao.addNewUsage(usage);
        return usage;
    }


    public Map<String, Long> getUsageByAccountForLastMonth(LocalDate invoiceDate, String customer) {
        return usageDetailsDao.getCurrentCycleUsageListByCustomerId(customer, JtcTime.localDateToInstant(invoiceDate.minusMonths(1)), JtcTime.localDateToInstant(invoiceDate)).stream()
                .collect(Collectors.groupingBy(Usage::getType))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().collect(Collectors.summarizingInt(Usage::getUsage)).getSum())
                );
    }


}
