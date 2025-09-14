package io.github.malczuuu.uiot.accounting.rest;

import io.github.malczuuu.uiot.accounting.core.AccountingService;
import io.github.malczuuu.uiot.accounting.model.AccountingTimeline;
import io.github.malczuuu.uiot.models.Pagination;
import io.github.malczuuu.uiot.models.TimeRange;
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
    TimeRange timeRange = TimeRange.parse(since, until);
    Pagination pagination = Pagination.parseSize(size, 50);
    return accountingService.getAccounting(timeRange, pagination);
  }

  @GetMapping(
      params = {"room_uid"},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AccountingTimeline getAccounting(
      @RequestParam("room_uid") String roomUid,
      @RequestParam(name = "since", defaultValue = "") String since,
      @RequestParam(name = "until", defaultValue = "") String until,
      @RequestParam(name = "size", defaultValue = "50") String size) {
    TimeRange timeRange = TimeRange.parse(since, until);
    Pagination pagination = Pagination.parseSize(size, 50);
    return accountingService.getAccounting(roomUid, timeRange, pagination);
  }
}
