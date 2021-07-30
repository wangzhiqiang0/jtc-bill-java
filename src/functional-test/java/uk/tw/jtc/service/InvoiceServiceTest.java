package uk.tw.jtc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import uk.tw.jtc.TestApp;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.dao.UsageDetailsDao;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.model.Usage;
import uk.tw.jtc.response.CurrentUsageAllowance;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.response.Pay;
import uk.tw.jtc.utils.HttpRequestBuilder;
import uk.tw.jtc.utils.TestUtils;
import uk.tw.jtc.utis.JtcTime;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
public class InvoiceServiceTest {
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    SubscriptDao subscriptDao;
    @Autowired
    UsageDetailsDao usageDetailsDao;

    @Test
    public void ShouldGenerateInvoiceOnBillDate() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now())));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        Invoice exceptedInvoice = new Invoice(customerId,new BigDecimal(38),4,2, Instant.now());
        List<Invoice> invoiceList= invoiceService.generateInvoice(LocalDate.now().plusMonths(1).minusDays(5));
        Invoice invoice = invoiceList.stream().filter(e->e.getCustomerId().equals(customerId)).findFirst().get();
        assertThat(exceptedInvoice).isEqualTo(invoice);
    }

    @Test
    public void ShouldGenerateInvoiceOnBillDateWhenExtra() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 20, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now())));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        Invoice exceptedInvoice = new Invoice(customerId,new BigDecimal(73),40,20, Instant.now());
        List<Invoice> invoiceList= invoiceService.generateInvoice(LocalDate.now().plusMonths(1).minusDays(5));
        Invoice invoice = invoiceList.stream().filter(e->e.getCustomerId().equals(customerId)).findFirst().get();
        assertThat(exceptedInvoice).isEqualTo(invoice);
    }

    @Test
    public void ShouldGenerateInvoiceOnBillDateWhenNotSmsUsage() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        Invoice exceptedInvoice = new Invoice(customerId,new BigDecimal(68),40,0, Instant.now());
        List<Invoice> invoiceList= invoiceService.generateInvoice(LocalDate.now().plusMonths(1).minusDays(5));
        Invoice invoice = invoiceList.stream().filter(e->e.getCustomerId().equals(customerId)).findFirst().get();
        assertThat(exceptedInvoice).isEqualTo(invoice);
    }
    @Test
    public void ShouldGenerateInvoiceNotOnBillDate() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 20, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        List<Invoice> invoiceList= invoiceService.generateInvoice(LocalDate.now().plusMonths(1).minusDays(6));
        assertThat(invoiceList.stream().filter(e->e.getCustomerId().equals(customerId)).collect(Collectors.toList())).isEqualTo(new ArrayList<>());
    }
    @Test
    public void ShouldGenerateInvoiceOnBillDateButNoUsage() {
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        Invoice exceptedInvoice = new Invoice(customerId,new BigDecimal(38),0,0, Instant.now());
        List<Invoice> invoiceList= invoiceService.generateInvoice(LocalDate.now().plusMonths(1).minusDays(5));
        Invoice invoice = invoiceList.stream().filter(e->e.getCustomerId().equals(customerId)).findFirst().get();
        assertThat(exceptedInvoice).isEqualTo(invoice);
    }
    @Test
    public void shouldGetInvoiceAllowanceWhenNoUsage(){
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        CurrentUsageAllowance exceptedCurrentUsageAllowance = new CurrentUsageAllowance(10,10);
        CurrentUsageAllowance currentUsageAllowance = invoiceService.getInvoiceAllowance(customerId);
        assertThat(currentUsageAllowance).isEqualTo(exceptedCurrentUsageAllowance);
    }
    @Test
    public void shouldGetInvoiceAllowanceWhenUsage(){
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(6))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        CurrentUsageAllowance exceptedCurrentUsageAllowance = new CurrentUsageAllowance(8,6);
        CurrentUsageAllowance currentUsageAllowance = invoiceService.getInvoiceAllowance(customerId);
        assertThat(currentUsageAllowance).isEqualTo(exceptedCurrentUsageAllowance);
    }
    @Test
    public void shouldGetInvoiceAllowanceWhenUsageExtra(){
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 6, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(1))));
            add(new Usage(customerId, 9, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 6, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(6))));
        }};

        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        CurrentUsageAllowance exceptedCurrentUsageAllowance = new CurrentUsageAllowance(1,0);
        CurrentUsageAllowance currentUsageAllowance = invoiceService.getInvoiceAllowance(customerId);
        assertThat(currentUsageAllowance).isEqualTo(exceptedCurrentUsageAllowance);
    }

    @Test
    public void shouldGetgetRealTimePay(){
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        int monthDayNum = LocalDate.now().lengthOfMonth();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-5))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-6))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(monthDayNum-4))));
        }};
        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        Pay excetedPay = new Pay(new BigDecimal(38));
        Pay pay = invoiceService.getRealTimePay(customerId);
        assertThat(excetedPay).isEqualTo(pay);
    }

    @Test
    public void shouldGetgetRealTimePayWhenExtra(){
        PackageInfo packageInfo = TestUtils.packageInfoList.get(0);
        String customerId = UUID.randomUUID().toString();
        List<Usage> usageList = new ArrayList<Usage>() {{
            add(new Usage(customerId, 6, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(1))));
            add(new Usage(customerId, 9, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(5))));
            add(new Usage(customerId, 6, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1).minusDays(5))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.PHONE.getType(), JtcTime.localDateToInstant(LocalDate.now().plusMonths(1))));
            add(new Usage(customerId, 2, UsageTypeEnum.SMS.getType(), JtcTime.localDateToInstant(LocalDate.now().minusDays(6))));
        }};
        //sub = date-5  thisInvoiceDate = this month date-5 --->next month date-5
        Subscript subscript = new Subscript(customerId, JtcTime.localDateToInstant(LocalDate.now().minusMonths(2).minusDays(5)), packageInfo);
        subscriptDao.createNewSubscript(subscript);
        usageList.forEach(usageDetailsDao::addNewUsage);
        Pay excetedPay = new Pay(new BigDecimal(40));
        Pay pay = invoiceService.getRealTimePay(customerId);
        assertThat(excetedPay).isEqualTo(pay);
    }


}
