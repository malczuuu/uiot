package io.github.malczuuu.uiot.accounting.core;

import io.github.malczuuu.uiot.accounting.model.AccountingRecord;
import io.github.malczuuu.uiot.accounting.model.AccountingTimeline;
import io.github.malczuuu.uiot.models.Pagination;
import io.github.malczuuu.uiot.models.TimeRange;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AccountingServiceImpl implements AccountingService {

  private final AccountingRepository accountingRepository;

  public AccountingServiceImpl(AccountingRepository accountingRepository) {
    this.accountingRepository = accountingRepository;
  }

  @Override
  public AccountingTimeline getAccounting(TimeRange timeRange, Pagination pagination) {
    Page<AccountingEntity> entities =
        accountingRepository.findAllByEndTimeBetweenOrderByStartTimeDesc(
            timeRange.getSinceInNano(),
            timeRange.getUntilInNano(),
            PageRequest.of(0, pagination.getSize()));
    return new AccountingTimeline(
        entities.stream().map(this::toRecord).collect(Collectors.toList()));
  }

  private AccountingRecord toRecord(AccountingEntity e) {
    return new AccountingRecord(
        e.getRoomUid(),
        e.getType(),
        e.getValue(),
        e.getStartTime() * 0.000_000_001,
        toIsoString(e.getStartTime()),
        e.getEndTime() * 0.000_000_001,
        toIsoString(e.getEndTime()),
        e.getTags());
  }

  private String toIsoString(Long time) {
    Instant instant = Instant.ofEpochSecond(time / 1000_000_000L, time % 1000_000_000L);
    OffsetDateTime dateTime = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    return dateTime.toString();
  }

  @Override
  public AccountingTimeline getAccounting(
      String roomUid, TimeRange timeRange, Pagination pagination) {
    Page<AccountingEntity> entities =
        accountingRepository.findAllByRoomUidAndEndTimeBetweenOrderByStartTimeDesc(
            roomUid,
            timeRange.getSinceInNano(),
            timeRange.getUntilInNano(),
            PageRequest.of(0, pagination.getSize()));
    return new AccountingTimeline(
        entities.stream().map(this::toRecord).collect(Collectors.toList()));
  }
}
