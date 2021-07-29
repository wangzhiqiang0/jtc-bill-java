package uk.tw.jtc.utis;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class JtcTime {
    private static final  int ZONE_HOURS = 8;
    public static Instant localDateToInstant(LocalDate date) {
        return date.atStartOfDay().toInstant(ZoneOffset.ofHours(ZONE_HOURS));
    }

    public static LocalDate instantToLocalDate(Instant instant) {
        return instant.atZone(ZoneId.ofOffset("UTC",ZoneOffset.ofHours(ZONE_HOURS))).toLocalDate();
    }


}
