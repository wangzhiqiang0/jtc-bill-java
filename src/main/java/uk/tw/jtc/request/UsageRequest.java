package uk.tw.jtc.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class UsageRequest {
    private int usage;
    private Instant incurredAt;
}
