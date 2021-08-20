package io.github.malczuuu.uiot.accounting.core;

import io.github.malczuuu.uiot.accounting.model.AccountingTimeline;

public interface AccountingService {

  AccountingTimeline getAccounting(long since, long until, int size);

  AccountingTimeline getAccounting(String roomUid, long since, long until, int size);
}
