package uk.tw.jtc.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pay {
    private BigDecimal pay;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pay)) return false;
        Pay pay1 = (Pay) o;
        return pay.equals(pay1.pay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pay);
    }
}
