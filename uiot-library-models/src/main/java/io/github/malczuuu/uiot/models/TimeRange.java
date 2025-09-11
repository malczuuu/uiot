package io.github.malczuuu.uiot.models;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public class TimeRange {

  public static TimeRange parse(String since, String until) {
    long sinceAsLong = parseTimeParameter(since, true);
    long untilAsLong = parseTimeParameter(until, false);
    return new TimeRange(sinceAsLong, untilAsLong);
  }

  private static long parseTimeParameter(String time, boolean isSince) {
    try {
      return (long) (Double.parseDouble(time) * 1000_000_000L);
    } catch (NumberFormatException e) {
      return fallbackThroughIso(time, isSince);
    }
  }

  private static long fallbackThroughIso(String time, boolean isSince) {
    OffsetDateTime dateTime;
    try {
      dateTime = OffsetDateTime.parse(time);
    } catch (DateTimeParseException e) {
      dateTime = isSince ? OffsetDateTime.MIN : OffsetDateTime.MAX;
    }
    Instant instant = dateTime.toInstant();
    return instant.getEpochSecond() * 1000_000_000L + instant.getNano();
  }

  private final long sinceInNano;
  private final long untilInNano;

  public TimeRange(long sinceInNano, long untilInNano) {
    this.sinceInNano = sinceInNano;
    this.untilInNano = untilInNano;
  }

  public long getSinceInNano() {
    return sinceInNano;
  }

  public long getUntilInNano() {
    return untilInNano;
  }
}
