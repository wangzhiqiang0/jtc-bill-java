package uk.tw.jtc.response;

import java.util.Objects;

public class CurrentBillingAllowance {
    private int smsAllowance;
    private int phoneAllowance;

    public int getSmsAllowance() {
        return smsAllowance;
    }

    public void setSmsAllowance(int smsAllowance) {
        this.smsAllowance = smsAllowance;
    }

    public int getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(int phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrentBillingAllowance)) return false;
        CurrentBillingAllowance that = (CurrentBillingAllowance) o;
        return smsAllowance == that.smsAllowance && phoneAllowance == that.phoneAllowance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(smsAllowance, phoneAllowance);
    }
}
