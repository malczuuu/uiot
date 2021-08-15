package io.github.malczuuu.uiot.accounting.core;

import io.github.malczuuu.uiot.accounting.model.AccountingPage;

public interface AccountingService {

  AccountingPage getAccounting(long since, long until, int size);

  AccountingPage getAccounting(String roomUid, long since, long until, int size);
}
