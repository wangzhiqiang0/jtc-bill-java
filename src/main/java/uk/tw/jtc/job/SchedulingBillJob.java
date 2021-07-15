package uk.tw.jtc.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.tw.jtc.service.BillingService;

@Component
public class SchedulingBillJob {
    @Autowired
    BillingService billingService;
    @Scheduled(cron = "0 0 0 * * ?")
    public void cronJob() {
        billingService.generateBill();
    }
}
