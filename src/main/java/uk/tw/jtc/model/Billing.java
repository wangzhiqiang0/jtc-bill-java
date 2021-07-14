package uk.tw.jtc.model;

import java.time.LocalDate;

public class Billing {
    private String billingId;
    private String customerId;
    private String packageId;
    private int phoneUsed;
    private int smsUsed;
    private int phonePay;
    private int smsPay;
    private LocalDate lastUpdateTime;

    public Billing(String billingId, String customerId, String packageId) {
        this.billingId = billingId;
        this.customerId = customerId;
        this.packageId = packageId;
    }

    public String getBillingId() {
        return billingId;
    }

    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public int getPhoneUsed() {
        return phoneUsed;
    }

    public void setPhoneUsed(int phoneUsed) {
        this.phoneUsed = phoneUsed;
    }

    public int getSmsUsed() {
        return smsUsed;
    }

    public void setSmsUsed(int smsUsed) {
        this.smsUsed = smsUsed;
    }

    public int getPhonePay() {
        return phonePay;
    }

    public void setPhonePay(int phonePay) {
        this.phonePay = phonePay;
    }

    public int getSmsPay() {
        return smsPay;
    }

    public void setSmsPay(int smsPay) {
        this.smsPay = smsPay;
    }

    public LocalDate getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDate lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
