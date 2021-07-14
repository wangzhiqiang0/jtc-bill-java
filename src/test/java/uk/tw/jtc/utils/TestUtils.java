package uk.tw.jtc.utils;

import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PackageReadingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {
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


       return new BillingService(generatePackageReadingService(),generateInvoiceService(),generateBillingDao());
    }
    public static InvoiceService generateInvoiceService() {


        return new InvoiceService(generateInvoiceDao());
    }

    private static InvoiceDao generateInvoiceDao() {
        return new InvoiceDao() {
            @Override
            public void createInvoice(Invoice invoice) {

            }

            @Override
            public void updateInvoice(Invoice invoice) {

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
                return generatePackageInfoList();
            }
        };
        return new PackageReadingService(packageReadingDao);
    }
}
