package io.github.malczuuu.uiot.accounting.core;

public interface UpsertAwareAccountingRepository {

  void upsert(AccountingEntity accountingEntity);
}
