package io.github.malczuuu.uiot.accounting.core;

import io.github.malczuuu.uiot.accounting.model.AccountingTimeline;
import io.github.malczuuu.uiot.models.Pagination;

public interface AccountingService {

  AccountingTimeline getAccounting(long since, long until, Pagination pagination);

  AccountingTimeline getAccounting(String roomUid, long since, long until, Pagination pagination);
}
