package io.github.malczuuu.uiot.accounting.core;

import io.github.malczuuu.uiot.accounting.model.AccountingModel;
import io.github.malczuuu.uiot.accounting.model.AccountingPage;
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
  public AccountingPage getAccounting(long since, long until, int size) {
    Page<AccountingEntity> entities =
        accountingRepository.findAllByEndTimeBetweenOrderByStartTimeDesc(
            since, until, PageRequest.of(0, size));
    return new AccountingPage(entities.stream().map(this::toRecord).collect(Collectors.toList()));
  }

  private AccountingModel toRecord(AccountingEntity e) {
    return new AccountingModel(
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
  public AccountingPage getAccounting(String roomUid, long since, long until, int size) {
    Page<AccountingEntity> entities =
        accountingRepository.findAllByRoomUidAndEndTimeBetweenOrderByStartTimeDesc(
            roomUid, since, until, PageRequest.of(0, size));
    return new AccountingPage(entities.stream().map(this::toRecord).collect(Collectors.toList()));
  }
}