package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.utils.TestUtils;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Test
    public void givenCustomerIdGetBillAtAnyTimeShouldReturnFee() {
        Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
        BigDecimal bg = new BigDecimal(30).multiply(new BigDecimal(0.5));
        invoice.setPay(bg);
        BillingDao billingDao = new BillingDao() {
            @Override
            public void createNewBill(Billing billing) {

            }

            @Override
            public void updateBill(Billing billing) {

            }

            @Override
            public Billing getBillByCustomerId(String customerId) {
                Billing billing = new Billing(UUID.randomUUID().toString(),customerId,TestUtils.packageInfoList.get(0).getPackageId());
                billing.setSmsUsed(20);
                billing.setPhoneUsed(20);
                billing.setSmsPay(10);
                billing.setPhonePay(10);
                billing.setExtraPhoneFee(new BigDecimal(1));
                billing.setExtraSMSFee(new BigDecimal(0.5));
                billing.setPhoneLimit(10);
                billing.setSmsLimit(10);
                billing.setSubscriptionFee(new BigDecimal(38));
                return billing;
            }

            @Override
            public void updatePhoneUsed(String billingId, int phoneUsed) {

            }

            @Override
            public void updateSMSUsed(String billingId, int smsUsed) {

            }

            @Override
            public void updatePhonePay(String billingId, int phonePay) {

            }

            @Override
            public void updateSMSPay(String billingId, int smsPay) {

            }

            @Override
            public List<Billing> getBillingList() {
                return null;
            }
        };
        BillingService bs = TestUtils.generateBillingService(billingDao);
        assertThat(bs.getBillAtAnyTime(TestUtils.CUSTOMER_ID).getPay()).isEqualTo(invoice.getPay());
    }

    @Test
    public void generateBillShouldReturn() {
        BillingDao billingDao = new BillingDao() {
            @Override
            public void createNewBill(Billing billing) {

            }

            @Override
            public void updateBill(Billing billing) {

            }

            @Override
            public Billing getBillByCustomerId(String customerId) {
                Billing billing = new Billing(UUID.randomUUID().toString(),customerId,TestUtils.packageInfoList.get(0).getPackageId());
                billing.setSmsUsed(20);
                billing.setPhoneUsed(20);
                billing.setSmsPay(10);
                billing.setPhonePay(10);
                billing.setExtraPhoneFee(new BigDecimal(1));
                billing.setExtraSMSFee(new BigDecimal(0.5));
                billing.setPhoneLimit(10);
                billing.setSmsLimit(10);
                billing.setSubscriptionFee(new BigDecimal(38));
                return billing;
            }

            @Override
            public void updatePhoneUsed(String billingId, int phoneUsed) {

            }

            @Override
            public void updateSMSUsed(String billingId, int smsUsed) {

            }

            @Override
            public void updatePhonePay(String billingId, int phonePay) {

            }

            @Override
            public void updateSMSPay(String billingId, int smsPay) {

            }

            @Override
            public List<Billing> getBillingList() {
                List<Billing> billingList = new ArrayList<>();
                PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
                Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,packageInfo.getPackageId());
                billing.setSmsUsed(1);
                billing.setPhoneUsed(1);

                billing.setExtraPhoneFee(packageInfo.getExtraPhoneFee());
                billing.setExtraSMSFee(packageInfo.getExtraSMSFee());
                billing.setPhoneLimit(packageInfo.getPhoneLimit());
                billing.setSmsLimit(packageInfo.getSmsLimit());
                billing.setSubscriptionFee(packageInfo.getSubscriptionFee());
                billingList.add(billing);
                billing.setSubscriptTime(LocalDate.now().minusDays(1));

                Billing billing1 = (Billing)billing.clone();
                billing1.setSubscriptTime(LocalDate.now().minusDays(LocalDate.now().minusMonths(1).lengthOfMonth()+1 ));
                billing1.setSmsUsed(20);
                billing1.setPhoneUsed(20);
                billing1.setPhonePay(10);
                billing1.setSmsPay(10);
                billing1.setCustomerId(UUID.randomUUID().toString());
                billing1.setBillingId(UUID.randomUUID().toString());
                billingList.add(billing1);

                return billingList;
            }
        };
        InvoiceDao invoiceDao = new InvoiceDao() {
            @Override
            public void createInvoice(Invoice invoice) {

            }

            @Override
            public void updateInvoice(Invoice invoice) {

            }

            @Override
            public Invoice getActiveInvoice(String customerId) {
                Invoice invoice = new Invoice(UUID.randomUUID().toString(),customerId);

                invoice.setPay(new BigDecimal(1));
                invoice.setStatus(PayEnum.PAID.getStatus());
                return invoice;
            }
        };
        BillingService bs = TestUtils.generateBillingService(invoiceDao,billingDao);
        assertThat(bs.generateBill()).isEqualTo("success");
    }

}
