package uk.tw.jtc.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Setter
@Getter
public class CurrentBillingAllowance {
    private int smsAllowance;
    private int phoneAllowance;

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
