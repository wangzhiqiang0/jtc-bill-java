package uk.tw.jtc.utils;

import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.dao.CustomerMapperPackageDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.CustomerMapperPackage;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.CustomerMapperPackageService;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PackageReadingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {
    public static final String CUSTOMER_ID = "10101010";
    public final static List<PackageInfo> packageInfoList;
    static {
        packageInfoList = generatePackageInfoList();
    }
    public static List<PackageInfo> generatePackageInfoList() {
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo(UUID.randomUUID().toString(),"Starter",new BigDecimal(38),
                10,10,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo(UUID.randomUUID().toString(),"Standard",new BigDecimal(58),
                30,40,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo(UUID.randomUUID().toString(),"Starter",new BigDecimal(188),
                300,200,new BigDecimal(1),new BigDecimal(0.5)));
        return packageInfoList;

    }

    public static BillingService generateBillingService() {


       return new BillingService(generatePackageReadingService(),generateInvoiceService(),
               generateCustomerMapperPackageService(),generateBillingDao());
    }
    public static BillingService generateBillingService(BillingDao billingDao) {


        return new BillingService(generatePackageReadingService(),generateInvoiceService(),
                generateCustomerMapperPackageService(),billingDao);
    }

    public static BillingService generateBillingService(InvoiceDao invoiceDao ,BillingDao billingDao) {


        return new BillingService(generatePackageReadingService(),generateInvoiceService(invoiceDao),
                generateCustomerMapperPackageService(),billingDao);
    }
    public static InvoiceService generateInvoiceService() {


        return new InvoiceService(generateInvoiceDao());
    }
    public static InvoiceService generateInvoiceService( InvoiceDao invoiceDao) {


        return new InvoiceService( invoiceDao);
    }

    public static CustomerMapperPackageService generateCustomerMapperPackageService() {


        return new CustomerMapperPackageService(generateCustomerMapperPackageDao());
    }

    private static CustomerMapperPackageDao generateCustomerMapperPackageDao() {
        return new CustomerMapperPackageDao() {
            @Override
            public void createCustomerMapperPackage(CustomerMapperPackage customerMapperPackage) {

            }
        };
    }

    private static InvoiceDao generateInvoiceDao() {
        return new InvoiceDao() {
            @Override
            public void createInvoice(Invoice invoice) {

            }

            @Override
            public void updateInvoice(Invoice invoice) {

            }

            @Override
            public Invoice getActiveInvoice(String customerId) {
                Invoice invoice = new Invoice(UUID.randomUUID().toString(),TestUtils.CUSTOMER_ID);
                invoice.setPay(TestUtils.packageInfoList.get(0).getSubscriptionFee());
                invoice.setStatus(PayEnum.ACTIVE.getStatus());
                invoice.setLastUpdateTime(LocalDate.now());
                return invoice;
            }
        };
    }

    public static BillingDao generateBillingDao() {
        BillingDao billingDao = new BillingDao() {
            @Override
            public void createNewBill(Billing billing) {

            }

            @Override
            public void updateBill(Billing billing) {

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

            @Override
            public Billing getBillByCustomerId(String customerId) {
                Billing billing = new Billing(UUID.randomUUID().toString(),customerId,packageInfoList.get(0).getPackageId());
                billing.setSmsUsed(5);
                billing.setPhoneUsed(5);
                return billing;
            }
        };
        return billingDao;
    }

    public static PackageReadingService generatePackageReadingService() {
        PackageReadingDao packageReadingDao = new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return null;
            }

            @Override
            public List<PackageInfo> listPackages() {
                return packageInfoList;
            }

            @Override
            public PackageInfo getPackageById(String packageId) {
                return packageInfoList.stream().filter(e -> e.getPackageId().equals(packageId)).findFirst().get();
            }
        };
        return new PackageReadingService(packageReadingDao);
    }
}
