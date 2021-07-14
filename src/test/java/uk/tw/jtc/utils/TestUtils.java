package uk.tw.jtc.utils;

import uk.tw.jtc.model.PackageInfo;

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
}
