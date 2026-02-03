package io.github.malczuuu.uiot.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", AccountingMetricEnvelope.TYPE})
public class AccountingMetricEnvelope extends Envelope {

  public static final String TYPE = "accounting_metric";

  private final AccountingMetric accountingMetric;

  @JsonCreator
  public AccountingMetricEnvelope(@JsonProperty(TYPE) AccountingMetric accountingMetric) {
    super(TYPE);
    this.accountingMetric = accountingMetric;
  }

  @JsonProperty(TYPE)
  public AccountingMetric getAccountingEvent() {
    return accountingMetric;
  }
}
