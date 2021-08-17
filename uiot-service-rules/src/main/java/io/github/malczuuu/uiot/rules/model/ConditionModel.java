package io.github.malczuuu.uiot.rules.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ConditionModel {

  private final List<String> thingUids;
  private final List<String> propertyNames;
  private final String operator;
  private final Double value;
  private final String valueString;
  private final Boolean valueBoolean;

  @JsonCreator
  public ConditionModel(
      @JsonProperty("thing_uids") List<String> thingUids,
      @JsonProperty("property_names") List<String> propertyNames,
      @JsonProperty("operator") String operator,
      @JsonProperty("value") Double value,
      @JsonProperty("value_string") String valueString,
      @JsonProperty("value_boolean") Boolean valueBoolean) {
    this.thingUids = thingUids;
    this.propertyNames = propertyNames;
    this.operator = operator;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
  }

  @JsonProperty("thing_uids")
  public List<String> getThingUids() {
    return thingUids;
  }

  @JsonProperty("property_names")
  public List<String> getPropertyNames() {
    return propertyNames;
  }

  @JsonProperty("operator")
  public String getOperator() {
    return operator;
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
}
