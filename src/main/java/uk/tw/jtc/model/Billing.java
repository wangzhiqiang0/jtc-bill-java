package uk.tw.jtc.model;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
@Setter
@Getter
public class Billing {
    private String billingId;
    private String customerId;
    private int phoneUsed;
    private int smsUsed;
    private int phonePay;
    private int smsPay;
    private boolean first;
    private LocalDate lastUpdateTime;
    private LocalDate subscriptTime;

    private PackageInfo packageInfo;

    public Billing(String billingId, String customerId, PackageInfo packageInfo) {
        this.billingId = billingId;
        this.customerId = customerId;
        this.packageInfo = packageInfo;
    }

    public Billing() {
    }

}
