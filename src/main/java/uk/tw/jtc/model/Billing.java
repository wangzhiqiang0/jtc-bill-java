package uk.tw.jtc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Billing {
    private String billingId;
    private String customerId;
    private Instant subscriptTime;
    private PackageInfo packageInfo;

    public Billing(String customerId, Instant subscriptTime,PackageInfo packageInfo) {
        this.billingId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.subscriptTime = subscriptTime;
        this.packageInfo = packageInfo;
    }

}
