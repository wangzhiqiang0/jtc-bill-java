package uk.tw.jtc.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class RequestInvoice {
    private String invoiceId;
    private BigDecimal pay;
}
