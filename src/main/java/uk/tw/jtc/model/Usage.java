package uk.tw.jtc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Usage {
    private String usageDetailsId;
    private String customerId;
    private int usage;
    private String type;
    private Instant incurredAt;
    Instant createdAt;

    public Usage(String customerId, int usage, String type, Instant incurredAt) {
        this.usageDetailsId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.usage = usage;
        this.type = type;
        this.incurredAt = incurredAt;
        this.createdAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usage)) return false;
        Usage tempUsage = (Usage) o;
        return usage == tempUsage.usage && customerId.equals(tempUsage.customerId) && type.equals(tempUsage.type) && incurredAt.equals(tempUsage.incurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, usage, type, incurredAt);
    }
}
