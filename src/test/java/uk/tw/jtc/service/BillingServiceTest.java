package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.utils.TestUtils;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BillingServiceTest {
    private BillingService billingService;

    @BeforeEach
    public void setUp() {
        billingService = TestUtils.generateBillingService();

    }

    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackage() {
        String exceptedStr = "success";
        assertThat(billingService.subscriptPackage(TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0).getPackageId())).isEqualTo(exceptedStr);
    }

    @Test
    public void givenCustomerIdAndPhoneUsedPhone() {
        String exceptedStr = "success";
        assertThat(billingService.usedPhone(TestUtils.CUSTOMER_ID,1)).isEqualTo(exceptedStr);
    }

    @Test
    public void givenCustomerIdAndSMSUsedSMS() {
        String exceptedStr = "success";
        assertThat(billingService.usedSMS(TestUtils.CUSTOMER_ID,1)).isEqualTo(exceptedStr);
    }

    @Test
    public void givenCustomerIdCurrentBillingPeriodShouldReturnAllowance() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(5);
        exceptedCurrentBillingAllowance.setPhoneAllowance(5);
        assertThat(billingService.currentBillingPeriod(TestUtils.CUSTOMER_ID)).isEqualTo(exceptedCurrentBillingAllowance);
    }

}
