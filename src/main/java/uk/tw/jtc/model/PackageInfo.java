package uk.tw.jtc.model;

import java.math.BigDecimal;

public class PackageInfo {
    private String packageId;
    private String name;
    private BigDecimal subscriptionFee;
    private int phoneLimit;
    private int smsLimit;
    private BigDecimal extraPhoneFee;
    private BigDecimal extraSMSFee;
    public PackageInfo() {
    }
    public PackageInfo(String packageId,String name, BigDecimal subscriptionFee, int phoneLimit, int smsLimit, BigDecimal extraPhoneFee, BigDecimal extraSMSFee) {
        this.packageId = packageId;
        this.name = name;
        this.subscriptionFee = subscriptionFee;
        this.phoneLimit = phoneLimit;
        this.smsLimit = smsLimit;
        this.extraPhoneFee = extraPhoneFee;
        this.extraSMSFee = extraSMSFee;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(BigDecimal subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
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

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
