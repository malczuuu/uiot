package io.github.malczuuu.uiot.models.accounting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountingEventEnvelope {

  private static final String TYPE = "accounting_event";

  private final AccountingEvent accountingEvent;

  @JsonCreator
  public AccountingEventEnvelope(@JsonProperty(TYPE) AccountingEvent accountingEvent) {
    this.accountingEvent = accountingEvent;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public AccountingEvent getAccountingEvent() {
    return accountingEvent;
  }
}
