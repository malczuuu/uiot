package io.github.malczuuu.uiot.accounting.core;

import io.github.malczuuu.uiot.accounting.model.AccountingTimeline;
import io.github.malczuuu.uiot.models.Pagination;
import io.github.malczuuu.uiot.models.TimeRange;

public interface AccountingService {

  AccountingTimeline getAccounting(TimeRange timeRange, Pagination pagination);

  AccountingTimeline getAccounting(String roomUid, TimeRange timeRange, Pagination pagination);
}
