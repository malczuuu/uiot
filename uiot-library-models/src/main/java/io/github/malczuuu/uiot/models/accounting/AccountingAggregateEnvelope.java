package io.github.malczuuu.uiot.models.accounting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountingAggregateEnvelope {

  private static final String TYPE = "accounting_aggregate";

  private final AccountingAggregate accountingAggregate;

  @JsonCreator
  public AccountingAggregateEnvelope(@JsonProperty(TYPE) AccountingAggregate accountingAggregate) {
    this.accountingAggregate = accountingAggregate;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public AccountingAggregate getWindowEvent() {
    return accountingAggregate;
  }
}
