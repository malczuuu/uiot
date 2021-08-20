package io.github.malczuuu.uiot.accounting.rest;

import io.github.malczuuu.uiot.accounting.core.AccountingService;
import io.github.malczuuu.uiot.accounting.model.AccountingTimeline;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/accounting")
public class AccountingController {

  private final AccountingService accountingService;

  public AccountingController(AccountingService accountingService) {
    this.accountingService = accountingService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public AccountingTimeline getAccounting(
      @RequestParam(name = "since", defaultValue = "") String since,
      @RequestParam(name = "until", defaultValue = "") String until,
      @RequestParam(name = "size", defaultValue = "50") String size) {
    long sinceAsLong = parseTimeParameter(since, true);
    long untilAsLong = parseTimeParameter(until, false);
    int sizeAsInt = parseSize(size);
    return accountingService.getAccounting(sinceAsLong, untilAsLong, sizeAsInt);
  }

  private long parseTimeParameter(String time, boolean isSince) {
    try {
      return (long) (Double.parseDouble(time) * 1000_000_000L);
    } catch (NumberFormatException e) {
      return fallbackThroughIso(time, isSince);
    }
  }

  private long fallbackThroughIso(String time, boolean isSince) {
    OffsetDateTime dateTime;
    try {
      dateTime = OffsetDateTime.parse(time);
    } catch (DateTimeParseException e) {
      dateTime = isSince ? OffsetDateTime.MIN : OffsetDateTime.MAX;
    }
    Instant instant = dateTime.toInstant();
    return instant.getEpochSecond() * 1000_000_000L + instant.getNano();
  }

  private int parseSize(String size) {
    try {
      return Integer.parseInt(size);
    } catch (NumberFormatException e) {
      return 50;
    }
  }

  @GetMapping(
      params = {"room_uid"},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AccountingTimeline getAccounting(
      @RequestParam("room_uid") String roomUid,
      @RequestParam(name = "since", defaultValue = "") String since,
      @RequestParam(name = "until", defaultValue = "") String until,
      @RequestParam(name = "size", defaultValue = "50") String size) {
    long sinceAsLong = parseTimeParameter(since, true);
    long untilAsLong = parseTimeParameter(until, false);
    int sizeAsInt = parseSize(size);
    return accountingService.getAccounting(roomUid, sinceAsLong, untilAsLong, sizeAsInt);
  }
}
