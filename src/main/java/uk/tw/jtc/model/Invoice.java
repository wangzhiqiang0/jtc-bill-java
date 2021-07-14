package uk.tw.jtc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getPay() {
        return pay;
    }

    public void setPay(BigDecimal pay) {
        this.pay = pay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDate lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
