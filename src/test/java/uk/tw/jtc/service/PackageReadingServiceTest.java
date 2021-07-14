package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class PackageReadingServiceTest {

    private static final String CUSTOMER_ID = "10101010";
    private PackageReadingService packageReadingService;
    final List<PackageInfo> packageInfoList = new ArrayList<>();
    @BeforeEach
    public void setUp() {
        packageInfoList.add(new PackageInfo("Starter",new BigDecimal(38),
                10,10,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo("Standard",new BigDecimal(58),
                30,40,new BigDecimal(1),new BigDecimal(0.5)));
        packageInfoList.add(new PackageInfo("Starter",new BigDecimal(188),
                300,200,new BigDecimal(1),new BigDecimal(0.5)));
        PackageReadingDao packageReadingDao = new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return packageInfoList.get(0);
            }

            @Override
            public List<PackageInfo> listPackages() {
                return packageInfoList;
            }
        };
        this.packageReadingService = new PackageReadingService(packageReadingDao);
    }
    @Test
    public void givenCustomerIdShouldReturnPackage() {
        assertThat(packageReadingService.getPackageByCustomerID(CUSTOMER_ID)).isEqualTo(packageInfoList.get(0));
    }
    @Test
    public void listPackagesShouldReturnPackageList() {
        assertThat(packageReadingService.listPackages()).isEqualTo(packageInfoList);
    }
}
