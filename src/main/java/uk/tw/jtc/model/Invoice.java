package uk.tw.jtc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
@Setter
@Getter
public class Invoice {
    private String invoiceId;
    private String customerId;
    private BigDecimal pay;
    private String status;
    private LocalDate lastUpdateTime;

    public Invoice(String invoiceId, String customerId) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        return customerId.equals(invoice.customerId) && pay.equals(invoice.pay) && status.equals(invoice.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, pay, status);
    }
}
