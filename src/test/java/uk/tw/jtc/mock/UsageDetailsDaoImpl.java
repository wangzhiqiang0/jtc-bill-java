package uk.tw.jtc.mock;

import lombok.Getter;
import lombok.Setter;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.model.Usage;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class UsageDetailsDaoImpl implements UsageDetailsDao {
    private List<Usage> usageList;
    @Override
    public List<Usage> getUsageListByCustomerId(String customerId) {
        return usageList.stream().filter(usage -> usage.getCustomerId().equals(customerId)).collect(Collectors.toList());

    }

    @Override
    public void addNewUsage(Usage usage) {
        usageList.add(usage);
    }

    @Override
    public List<Usage> getCurrentCycleUsageListByCustomerId(String customerId, Instant start, Instant end) {
        return usageList.stream().filter(usage -> usage.getCustomerId().equals(customerId) &&
                usage.getIncurredAt().compareTo(start) >=0 &&
                usage.getIncurredAt().compareTo(end) <0).collect(Collectors.toList());
    }
}
