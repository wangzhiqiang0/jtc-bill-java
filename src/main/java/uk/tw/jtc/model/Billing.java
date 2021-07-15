package uk.tw.jtc.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Billing  implements Cloneable{
    private String billingId;
    private String customerId;
    private String packageId;
    private int phoneUsed;
    private int smsUsed;
    private int phonePay;
    private int smsPay;
    private LocalDate lastUpdateTime;
    private LocalDate subscriptTime;

    private int phoneLimit;
    private int smsLimit;
    private BigDecimal extraPhoneFee;
    private BigDecimal extraSMSFee;
    private BigDecimal subscriptionFee;

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

    public LocalDate getSubscriptTime() {
        return subscriptTime;
    }

    public void setSubscriptTime(LocalDate subscriptTime) {
        this.subscriptTime = subscriptTime;
    }

    public int getPhoneLimit() {
        return phoneLimit;
    }

    public void setPhoneLimit(int phoneLimit) {
        this.phoneLimit = phoneLimit;
    }

    public int getSmsLimit() {
        return smsLimit;
    }

    public void setSmsLimit(int smsLimit) {
        this.smsLimit = smsLimit;
    }

    public BigDecimal getExtraPhoneFee() {
        return extraPhoneFee;
    }

    public void setExtraPhoneFee(BigDecimal extraPhoneFee) {
        this.extraPhoneFee = extraPhoneFee;
    }

    public BigDecimal getExtraSMSFee() {
        return extraSMSFee;
    }

    public void setExtraSMSFee(BigDecimal extraSMSFee) {
        this.extraSMSFee = extraSMSFee;
    }

    public BigDecimal getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(BigDecimal subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    @Override
    public Object clone() {
        Billing stu = null;
        try{
            stu = (Billing)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
