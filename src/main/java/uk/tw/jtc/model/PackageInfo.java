package uk.tw.jtc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PackageInfo)) return false;
        PackageInfo that = (PackageInfo) o;
        return phoneLimit == that.phoneLimit && smsLimit == that.smsLimit && name.equals(that.name) && subscriptionFee.equals(that.subscriptionFee) && extraPhoneFee.equals(that.extraPhoneFee) && extraSMSFee.equals(that.extraSMSFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subscriptionFee, phoneLimit, smsLimit, extraPhoneFee, extraSMSFee);
    }
}
