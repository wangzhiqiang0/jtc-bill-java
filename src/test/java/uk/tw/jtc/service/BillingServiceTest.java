package uk.tw.jtc.service;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.mock.BillingDaoImpl;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.mock.PackageReadingDaoImpl;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.utils.TestUtils;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BillingServiceTest {
    private BillingService billingService;
    private BillingDaoImpl billingDao;
    @BeforeEach
    public void setUp() {
        billingDao = new BillingDaoImpl();
        billingDao.setBillingList(new ArrayList<>());
        PackageReadingDaoImpl packageReadingDao = new PackageReadingDaoImpl();
        InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
        invoiceDao.setInvoicesList(new ArrayList<>());
        PackageReadingService packageReadingService = new PackageReadingService(packageReadingDao);
        InvoiceService invoiceService = new InvoiceService(invoiceDao);
        billingService = new BillingService(packageReadingService,invoiceService,billingDao);

    }

    @Test
    public void givenCustomerIdAndPackageIdSubscriptPackage() {
        Billing billing = billingService.subscriptPackage(TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0).getPackageId());

        assertThat(billing.getCustomerId()).isEqualTo(TestUtils.CUSTOMER_ID);
        assertThat(billingDao.getBillingList().stream().filter(e -> e.getBillingId().equals(billing.getBillingId())).findFirst().get()).isEqualTo(billing);

    }

    @Test
    public void givenCustomerIdCurrentBillingPeriodWhenSmsAndPhoneRemainderShouldReturnAllowance() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(6);
        exceptedCurrentBillingAllowance.setPhoneAllowance(7);
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(14);
        billing.setPhoneUsed(13);
        billing.setSmsPay(10);
        billing.setPhonePay(10);
        billingDao.getBillingList().add(billing);
        assertThat(billingService.currentBillingPeriod(TestUtils.CUSTOMER_ID)).isEqualTo(exceptedCurrentBillingAllowance);
    }

    @Test
    public void givenCustomerIdCurrentBillingPeriodWhenSmsRemainderShouldReturnAllowance() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(6);
        exceptedCurrentBillingAllowance.setPhoneAllowance(0);
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(14);
        billing.setPhoneUsed(21);
        billing.setSmsPay(10);
        billing.setPhonePay(10);
        billingDao.getBillingList().add(billing);
        assertThat(billingService.currentBillingPeriod(TestUtils.CUSTOMER_ID)).isEqualTo(exceptedCurrentBillingAllowance);
    }

    @Test
    public void givenCustomerIdCurrentBillingPeriodWhenPhoneRemainderShouldReturnAllowance() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(0);
        exceptedCurrentBillingAllowance.setPhoneAllowance(5);
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(21);
        billing.setPhoneUsed(15);
        billing.setSmsPay(10);
        billing.setPhonePay(10);
        billingDao.getBillingList().add(billing);
        assertThat(billingService.currentBillingPeriod(TestUtils.CUSTOMER_ID)).isEqualTo(exceptedCurrentBillingAllowance);
    }

    @Test
    public void givenCustomerIdCurrentBillingPeriodWhenNoRemainderShouldReturnAllowance() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(0);
        exceptedCurrentBillingAllowance.setPhoneAllowance(0);
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(21);
        billing.setPhoneUsed(22);
        billing.setSmsPay(10);
        billing.setPhonePay(10);
        billingDao.getBillingList().add(billing);
        assertThat(billingService.currentBillingPeriod(TestUtils.CUSTOMER_ID)).isEqualTo(exceptedCurrentBillingAllowance);
    }

    @Test
    public void givenCustomerIdCurrentBillingPeriodWhenNoUsedShouldReturnAllowance() {
        CurrentBillingAllowance exceptedCurrentBillingAllowance = new CurrentBillingAllowance();
        exceptedCurrentBillingAllowance.setSmsAllowance(10);
        exceptedCurrentBillingAllowance.setPhoneAllowance(10);
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(0);
        billing.setPhoneUsed(0);
        billing.setSmsPay(0);
        billing.setPhonePay(0);
        billingDao.getBillingList().add(billing);
        assertThat(billingService.currentBillingPeriod(TestUtils.CUSTOMER_ID)).isEqualTo(exceptedCurrentBillingAllowance);
    }

    @Test
    public void generateBillShouldReturn() {
        List<String> customerList = new ArrayList<>();
        List<Billing> billingList = getBillingList();
        billingDao.setBillingList(billingList);
        billingList.forEach(e -> customerList.add(e.getCustomerId()));

        List<Invoice> invoiceList = billingService.generateBill();

        //fist pay,should pay 38 and first change to false
        Invoice invoice1 = invoiceList.stream().filter(e->e.getCustomerId().equals(customerList.get(0))).findFirst().get();
        assertThat(invoice1.getPay().doubleValue()).isEqualTo(38);
        assertThat(billingDao.getBillByCustomerId(customerList.get(0)).isFirst()).isEqualTo(false);
        assertThat(billingDao.getBillByCustomerId(customerList.get(0)).getPhonePay()).isEqualTo(0);
        assertThat(billingDao.getBillByCustomerId(customerList.get(0)).getSmsPay()).isEqualTo(0);

        //not in pay day , not pay
        Optional<Invoice> invoice2 = invoiceList.stream().filter(e->e.getCustomerId().equals(customerList.get(1))).findFirst();
        assertThat(invoice2.isPresent()).isEqualTo(false);

        //second pay , sms and phone less than limit, should pay 38,and bill phonePay change to 5,smsPay change to 5
        Invoice invoice3 = invoiceList.stream().filter(e->e.getCustomerId().equals(customerList.get(2))).findFirst().get();
        assertThat(invoice3.getPay().doubleValue()).isEqualTo(38);
        assertThat(billingDao.getBillByCustomerId(customerList.get(2)).isFirst()).isEqualTo(false);
        assertThat(billingDao.getBillByCustomerId(customerList.get(2)).getPhonePay()).isEqualTo(5);
        assertThat(billingDao.getBillByCustomerId(customerList.get(2)).getSmsPay()).isEqualTo(5);

        //second pay , sms and phone more than limit,should pay 38+1*10+0.5*10,and bill phonePay change to 20,smsPay change to 20
        Invoice invoice4 = invoiceList.stream().filter(e->e.getCustomerId().equals(customerList.get(3))).findFirst().get();
        assertThat(invoice4.getPay().doubleValue()).isEqualTo(53);
        assertThat(billingDao.getBillByCustomerId(customerList.get(3)).isFirst()).isEqualTo(false);
        assertThat(billingDao.getBillByCustomerId(customerList.get(3)).getPhonePay()).isEqualTo(20);
        assertThat(billingDao.getBillByCustomerId(customerList.get(3)).getSmsPay()).isEqualTo(20);

        //second pay , sms more than limit ,phone  less than limit,should pay 58+10*0.5,and bill phonePay change to 20,smsPay change to 50
        Invoice invoice5 = invoiceList.stream().filter(e->e.getCustomerId().equals(customerList.get(4))).findFirst().get();
        assertThat(invoice5.getPay().doubleValue()).isEqualTo(63.0);
        assertThat(billingDao.getBillByCustomerId(customerList.get(4)).isFirst()).isEqualTo(false);
        assertThat(billingDao.getBillByCustomerId(customerList.get(4)).getPhonePay()).isEqualTo(20);
        assertThat(billingDao.getBillByCustomerId(customerList.get(4)).getSmsPay()).isEqualTo(50);


        //second pay , phone more than limit ,sms  less than limit,should pay 58+1*10,and bill phonePay change to 40,smsPay change to 30
        Invoice invoice6 = invoiceList.stream().filter(e->e.getCustomerId().equals(customerList.get(5))).findFirst().get();
        assertThat(invoice6.getPay().doubleValue()).isEqualTo(68);
        assertThat(billingDao.getBillByCustomerId(customerList.get(5)).isFirst()).isEqualTo(false);
        assertThat(billingDao.getBillByCustomerId(customerList.get(5)).getPhonePay()).isEqualTo(40);
        assertThat(billingDao.getBillByCustomerId(customerList.get(5)).getSmsPay()).isEqualTo(30);

        //third pay , phone more than limit ,sms  more than limit,should pay 38+1*10+0.5*10,and bill phonePay change to 40,smsPay change to 40
        Invoice invoice7 = invoiceList.stream().filter(e->e.getCustomerId().equals(customerList.get(6))).findFirst().get();
        assertThat(invoice7.getPay().doubleValue()).isEqualTo(53.0);
        assertThat(billingDao.getBillByCustomerId(customerList.get(6)).isFirst()).isEqualTo(false);
        assertThat(billingDao.getBillByCustomerId(customerList.get(6)).getPhonePay()).isEqualTo(40);
        assertThat(billingDao.getBillByCustomerId(customerList.get(6)).getSmsPay()).isEqualTo(40);
    }

    private List<Billing> getBillingList() {
        List<Billing> billingList = new ArrayList<>();
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);

        //fist pay,should pay 38,and first change to false
        Billing billing = new Billing(UUID.randomUUID().toString(),UUID.randomUUID().toString(),packageInfo);
        billing.setSmsUsed(1);
        billing.setPhoneUsed(1);
        billing.setFirst(true);
        billing.setSubscriptTime(LocalDate.now().minusDays(1));


        //not in pay day , not pay
        Billing billing1 = new Billing(UUID.randomUUID().toString(),UUID.randomUUID().toString(),packageInfo);
        billing1.setSmsUsed(5);
        billing1.setPhoneUsed(5);
        billing1.setFirst(false);
        billing1.setSubscriptTime(LocalDate.now().plusDays(1));


        //second pay , sms and phone less than limit, should pay 38,and bill phonePay change to 5,smsPay change to 5
        Billing billing2 = new Billing(UUID.randomUUID().toString(),UUID.randomUUID().toString(),packageInfo);
        billing2.setSmsUsed(5);
        billing2.setPhoneUsed(5);
        billing2.setFirst(false);
        billing2.setSubscriptTime(LocalDate.now().minusMonths(1));


        //second pay , sms and phone more than limit,should pay 38+1*10+0.5*10,and bill phonePay change to 20,smsPay change to 20
        Billing billing3 = new Billing(UUID.randomUUID().toString(),UUID.randomUUID().toString(),packageInfo);
        billing3.setSmsUsed(20);
        billing3.setPhoneUsed(20);
        billing3.setFirst(false);
        billing3.setSubscriptTime(LocalDate.now().minusMonths(1));

        //second pay , sms more than limit ,phone  less than limit,should pay 58+10*0.5,and bill phonePay change to 20,smsPay change to 50
        Billing billing4 = new Billing(UUID.randomUUID().toString(),UUID.randomUUID().toString(),TestUtils.packageInfoList.get(1));
        billing4.setSmsUsed(50);
        billing4.setPhoneUsed(20);
        billing4.setFirst(false);
        billing4.setSubscriptTime(LocalDate.now().minusMonths(1));


        //second pay , phone more than limit ,sms  less than limit,should pay 58+1*10,and bill phonePay change to 40,smsPay change to 30
        Billing billing5 = new Billing(UUID.randomUUID().toString(),UUID.randomUUID().toString(),TestUtils.packageInfoList.get(1));
        billing5.setSmsUsed(30);
        billing5.setPhoneUsed(40);
        billing5.setFirst(false);
        billing5.setSubscriptTime(LocalDate.now().minusMonths(1));

        //third pay , phone more than limit ,sms  more than limit,should pay 38+1*10+0.5*10,and bill phonePay change to 40,smsPay change to 40
        Billing billing6 = new Billing(UUID.randomUUID().toString(),UUID.randomUUID().toString(),packageInfo);
        billing6.setSmsUsed(40);
        billing6.setPhoneUsed(40);
        billing6.setSmsPay(20);
        billing6.setPhonePay(20);
        billing6.setFirst(false);
        billing6.setSubscriptTime(LocalDate.now().minusMonths(1));

        billingList.add(billing);
        billingList.add(billing1);
        billingList.add(billing2);
        billingList.add(billing3);
        billingList.add(billing4);
        billingList.add(billing5);
        billingList.add(billing6);

        return billingList;


    }


    @Test
    public void givenCustomerIdAndPhoneUsedPhone() {
        List<Billing> billingList = new ArrayList<>();
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(1);
        billing.setPhoneUsed(1);
        billing.setFirst(true);
        billing.setSubscriptTime(LocalDate.now());
        billingList.add(billing);
        billingDao.setBillingList(billingList);
        assertThat(billingService.usedPhone(TestUtils.CUSTOMER_ID,10).getPhoneUsed()).isEqualTo(11);
    }

    @Test
    public void givenCustomerIdAndSMSUsedSMS() {
        List<Billing> billingList = new ArrayList<>();
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,TestUtils.packageInfoList.get(0));
        billing.setSmsUsed(1);
        billing.setPhoneUsed(1);
        billing.setFirst(true);
        billing.setSubscriptTime(LocalDate.now());
        billingList.add(billing);
        billingDao.setBillingList(billingList);
        assertThat(billingService.usedSMS(TestUtils.CUSTOMER_ID,10).getSmsUsed()).isEqualTo(11);
    }

    @Test
    public void givenCustomerIdGetBillAtAnyTimeShouldReturn() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        List<Billing> billingList = new ArrayList<>();
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,packageInfo);
        billing.setSmsUsed(1);
        billing.setPhoneUsed(1);
        billing.setFirst(true);
        billing.setSubscriptTime(LocalDate.now());
        billingList.add(billing);
        billingDao.setBillingList(billingList);
        assertThat(billingService.getBillAtAnyTime(TestUtils.CUSTOMER_ID).getPay()).isEqualTo(packageInfo.getSubscriptionFee());
    }
    @Test
    public void givenCustomerIdGetBillAtAnyTimeNotFirstShouldReturn() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        List<Billing> billingList = new ArrayList<>();
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,packageInfo);
        billing.setSmsUsed(5);
        billing.setPhoneUsed(5);
        billing.setFirst(false);
        billing.setSubscriptTime(LocalDate.now());
        billingList.add(billing);
        billingDao.setBillingList(billingList);
        assertThat(billingService.getBillAtAnyTime(TestUtils.CUSTOMER_ID).getPay()).isEqualTo(packageInfo.getSubscriptionFee());
    }

    @Test
    public void givenCustomerIdGetBillAtAnyTimeNotFirstMoreLimitShouldReturn() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        List<Billing> billingList = new ArrayList<>();
        Billing billing = new Billing(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID,packageInfo);
        billing.setSmsUsed(15);
        billing.setPhoneUsed(15);
        billing.setFirst(false);
        billing.setSubscriptTime(LocalDate.now());
        billingList.add(billing);
        billingDao.setBillingList(billingList);
        double expectedPay = packageInfo.getSubscriptionFee().
                add(packageInfo.getExtraSMSFee().multiply(new BigDecimal(5)).add(packageInfo.getExtraPhoneFee().multiply(new BigDecimal(5)))).doubleValue();
        assertThat(billingService.getBillAtAnyTime(TestUtils.CUSTOMER_ID).getPay().doubleValue()).
                isEqualTo(expectedPay);
    }





}
