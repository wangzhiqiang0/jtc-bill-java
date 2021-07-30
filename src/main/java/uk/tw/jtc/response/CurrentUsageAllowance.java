package uk.tw.jtc.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUsageAllowance {
    private int smsAllowance;
    private int phoneAllowance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrentUsageAllowance)) return false;
        CurrentUsageAllowance that = (CurrentUsageAllowance) o;
        return smsAllowance == that.smsAllowance && phoneAllowance == that.phoneAllowance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(smsAllowance, phoneAllowance);
    }
}
