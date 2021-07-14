package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;

import java.math.BigDecimal;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class PackageReadingServiceTest {

    private static final String CUSTOMER_ID = "10101010";
    private PackageReadingService packageReadingService;
    PackageInfo  packageInfo;
    @BeforeEach
    public void setUp() {
        packageInfo = new PackageInfo("Starter",new BigDecimal(38),
                10,10,new BigDecimal(1),new BigDecimal(0.5));
        PackageReadingDao packageReadingDao = new PackageReadingDao() {
            @Override
            public PackageInfo getPackageByCustomerID(String customerID) {
                return packageInfo;
            }
        };
        this.packageReadingService = new PackageReadingService(packageReadingDao);
    }
    @Test
    public void givenCustomerIdShouldReturnPackage() {
        assertThat(packageReadingService.getPackageByCustomerID(CUSTOMER_ID)).isEqualTo(packageInfo);
    }
}
