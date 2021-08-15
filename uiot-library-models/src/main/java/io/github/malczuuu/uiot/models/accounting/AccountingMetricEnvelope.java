package io.github.malczuuu.uiot.models.accounting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountingMetricEnvelope {

  private static final String TYPE = "accounting_metric";

  private final AccountingMetric accountingMetric;

  @JsonCreator
  public AccountingMetricEnvelope(@JsonProperty(TYPE) AccountingMetric accountingMetric) {
    this.accountingMetric = accountingMetric;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public AccountingMetric getAccountingEvent() {
    return accountingMetric;
  }
}
