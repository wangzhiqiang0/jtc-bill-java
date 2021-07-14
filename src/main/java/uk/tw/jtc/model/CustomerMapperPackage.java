package uk.tw.jtc.model;

import java.time.LocalDate;

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

    public String getCustomerMapperPackageId() {
        return customerMapperPackageId;
    }

    public void setCustomerMapperPackageId(String customerMapperPackageId) {
        this.customerMapperPackageId = customerMapperPackageId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getSubscriptTime() {
        return subscriptTime;
    }

    public void setSubscriptTime(LocalDate subscriptTime) {
        this.subscriptTime = subscriptTime;
    }
}
