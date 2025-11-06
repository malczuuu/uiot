package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", AccountingWindowEnvelope.TYPE})
public class AccountingWindowEnvelope extends Envelope {

  public static final String TYPE = "accounting_window";

  private final AccountingWindow accountingWindow;

  @JsonCreator
  public AccountingWindowEnvelope(@JsonProperty(TYPE) AccountingWindow accountingWindow) {
    super(TYPE);
    this.accountingWindow = accountingWindow;
  }

  @JsonProperty(TYPE)
  public AccountingWindow getWindowEvent() {
    return accountingWindow;
  }
}
