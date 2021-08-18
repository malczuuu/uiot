package io.github.malczuuu.uiot.models.rule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionExecutionEvent {

  private final String thingUid;
  private final String propertyName;
  private final String ruleUid;
  private final Double value;
  private final String valueString;
  private final Boolean valueBoolean;
  private final String message;

  @JsonCreator
  public ActionExecutionEvent(
      @JsonProperty("thing_uid") String thingUid,
      @JsonProperty("property_name") String propertyName,
      @JsonProperty("rule_uid") String ruleUid,
      @JsonProperty("value") Double value,
      @JsonProperty("value_string") String valueString,
      @JsonProperty("value_boolean") Boolean valueBoolean,
      @JsonProperty("message") String message) {
    this.thingUid = thingUid;
    this.propertyName = propertyName;
    this.ruleUid = ruleUid;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
    this.message = message;
  }

  @JsonProperty("thing_uid")
  public String getThingUid() {
    return thingUid;
  }

  @JsonProperty("property_name")
  public String getPropertyName() {
    return propertyName;
  }

  @JsonProperty("rule_uid")
  public String getRuleUid() {
    return ruleUid;
  }

  @JsonProperty("value")
  public Double getValue() {
    return value;
  }

  @JsonProperty("value_string")
  public String getValueString() {
    return valueString;
  }

  @JsonProperty("value_boolean")
  public Boolean getValueBoolean() {
    return valueBoolean;
  }

  @JsonProperty("message")
  public String getMessage() {
    return message;
  }
}
