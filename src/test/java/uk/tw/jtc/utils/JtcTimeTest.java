package uk.tw.jtc.utils;

import org.junit.jupiter.api.Test;
import uk.tw.jtc.utis.JtcTime;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JtcTimeTest {
    @Test
    public void shouldGiveLocalDateThenReturnInstant() {
        assertThat(JtcTime.
                localDateToInstant(LocalDate.of(2021, 9, 17))).isEqualTo(Instant.parse("2021-09-16T16:00:00Z"));

    }

    @Test
    public void shouldGiveInstantThenReturnLocalDate() {
        assertThat(JtcTime.instantToLocalDate(Instant.parse("2021-09-16T16:00:00Z"))).isEqualTo(LocalDate.of(2021, 9, 17));

    }

}
