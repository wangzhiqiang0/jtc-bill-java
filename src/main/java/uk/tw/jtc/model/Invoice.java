package uk.tw.jtc.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Setter
@Getter
public class Invoice {
    private String invoiceId;
    private String customerId;
    private BigDecimal pay;
    private int phoneUsage;
    private int smsUsage;
    private Instant createTime;

    public Invoice(String customerId, BigDecimal pay, int phoneUsage, int smsUsage, Instant createTime) {
        this.customerId = customerId;
        this.pay = pay;
        this.phoneUsage = phoneUsage;
        this.smsUsage = smsUsage;
        this.createTime = createTime;
    }

    public Invoice(String invoiceId, String customerId) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
    }

    public Invoice() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        return phoneUsage == invoice.phoneUsage && smsUsage == invoice.smsUsage && customerId.equals(invoice.customerId) && pay.equals(invoice.pay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, pay, phoneUsage, smsUsage);
    }
}
