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
        packageInfoList.add(new PackageInfo("39a62e4e-07d4-4940-a60b-c44aafe00dad","Starter",new BigDecimal(38),
                10,10,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo("221b3291-5376-45a8-85a1-2dcee83c5820","Standard",new BigDecimal(58),
                30,40,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo("b85e170e-fc55-4311-bb81-88c1f92c7bb4","Starter",new BigDecimal(188),
                300,200,new BigDecimal(1),new BigDecimal(0.5)));
        return packageInfoList;

    }
}
