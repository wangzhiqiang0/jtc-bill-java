package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.utils.TestUtils;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BillingServiceTest {
    private static final String CUSTOMER_ID = "10101010";
    private BillingService billingService;
    List<PackageInfo> packageInfoList;
    @BeforeEach
    public void setUp() {
        packageInfoList = TestUtils.generatePackageInfoList();
        billingService = TestUtils.generateBillingService();

    }

    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackage() {
        String exceptedStr = "success";
        assertThat(billingService.subscriptPackage(CUSTOMER_ID,packageInfoList.get(0).getPackageId())).isEqualTo(exceptedStr);
    }
}
