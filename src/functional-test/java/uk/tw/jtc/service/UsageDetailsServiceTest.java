package uk.tw.jtc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.tw.jtc.TestApp;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Usage;
import uk.tw.jtc.utils.TestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
public class UsageDetailsServiceTest {

    @Autowired
    UsageDetailsDao usageDetailsDao;

    @Autowired
    SubscriptDao subscriptDao;
    @Autowired
    UsageDetailsService usageDetailsService;

    @Test
    public void ShouldGetBasicSubscriptionFeeWhenSMSandPhoneDoesNotExceed() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        int day = LocalDate.now().lengthOfMonth() - 1;
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 1, UsageTypeEnum.SMS.getType(), Instant.parse("2021-08-16T14:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-08-16T17:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.SMS.getType(), Instant.parse("2021-08-20T14:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-09-16T14:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-09-16T17:00:00Z")));
            add(new Usage(customerId, 1, UsageTypeEnum.PHONE.getType(), Instant.parse("2021-07-17T00:00:00Z")));

        }};
        subscriptDao.createNewSubscript(new Subscript(customerId, Instant.now().minusSeconds(60 * 60 * 24 * day), packageInfo));
        usageList.forEach(usageDetailsDao::addNewUsage);
        Map<String, Long> usagesMap = usageDetailsService.getUsageByAccountForLastMonth(LocalDate.of(2021, 9, 17), customerId);
        assertThat(usagesMap.get(UsageTypeEnum.PHONE.getType()
        )).isEqualTo(2);
        assertThat(usagesMap.get(UsageTypeEnum.SMS.getType()
        )).isEqualTo(1);


    }
}
