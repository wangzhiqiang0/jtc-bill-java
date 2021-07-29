package uk.tw.jtc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Subscript {
    private String subscriptId;
    private String customerId;
    private Instant subscriptTime;
    private PackageInfo packageInfo;

    public Subscript(String customerId, Instant subscriptTime, PackageInfo packageInfo) {
        this.subscriptId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.subscriptTime = subscriptTime;
        this.packageInfo = packageInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscript)) return false;
        Subscript subscript = (Subscript) o;
        return customerId.equals(subscript.customerId) && subscriptTime.equals(subscript.subscriptTime) && packageInfo.equals(subscript.packageInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, subscriptTime, packageInfo);
    }
}
