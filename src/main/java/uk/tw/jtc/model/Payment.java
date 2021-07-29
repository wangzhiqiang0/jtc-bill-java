package uk.tw.jtc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Payment {
    private String paymentId;
    private String invoiceId;
    private String customerId;
    private BigDecimal pay;
    private Instant createdAt;

    public Payment(String invoiceId, String customerId, BigDecimal pay) {
        this.paymentId = UUID.randomUUID().toString();
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.pay = pay;
        this.createdAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return paymentId.equals(payment.paymentId) && invoiceId.equals(payment.invoiceId) && customerId.equals(payment.customerId) && pay.equals(payment.pay) && createdAt.equals(payment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, invoiceId, customerId, pay, createdAt);
    }
}
