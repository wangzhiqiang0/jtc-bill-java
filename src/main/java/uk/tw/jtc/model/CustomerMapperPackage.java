package uk.tw.jtc.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
public class CustomerMapperPackage {
    private String customerMapperPackageId;
    private String packageId;
    private String customerId;
    private LocalDate subscriptTime;

    public CustomerMapperPackage(String customerMapperPackageId, String packageId, String customerId, LocalDate subscriptTime) {
        this.customerMapperPackageId = customerMapperPackageId;
        this.packageId = packageId;
        this.customerId = customerId;
        this.subscriptTime = subscriptTime;
    }


}
