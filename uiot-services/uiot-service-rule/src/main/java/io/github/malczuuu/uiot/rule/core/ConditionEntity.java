package io.github.malczuuu.uiot.rule.core;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

public class ConditionEntity {

  public static final String THING_UIDS = "thingUids";
  public static final String PROPERTY_NAMES = "propertyNames";
  public static final String OPERATOR = "operator";
  public static final String VALUE = "value";
  public static final String VALUE_STRING = "valueString";
  public static final String VALUE_BOOLEAN = "valueBoolean";

  @Field("thingUids")
  private List<String> thingUids;

  @Field("propertyNames")
  private List<String> propertyNames;

  @Field("operator")
  private OperatorEntity operator;

  @Field("value")
  private Double value;

  @Field("valueString")
  private String valueString;

  @Field("valueBoolean")
  private Boolean valueBoolean;

  public ConditionEntity() {}

  public ConditionEntity(
      List<String> thingUids, List<String> propertyNames, OperatorEntity operator, Double value) {
    this(thingUids, propertyNames, operator, value, null, null);
  }

  public ConditionEntity(
      List<String> thingUids,
      List<String> propertyNames,
      OperatorEntity operator,
      String valueString) {
    this(thingUids, propertyNames, operator, null, valueString, null);
  }

  public ConditionEntity(
      List<String> thingUids,
      List<String> propertyNames,
      OperatorEntity operator,
      Boolean valueBoolean) {
    this(thingUids, propertyNames, operator, null, null, valueBoolean);
  }

  public ConditionEntity(
      List<String> thingUids,
      List<String> propertyNames,
      OperatorEntity operator,
      Double value,
      String valueString,
      Boolean valueBoolean) {
    this.thingUids = new ArrayList<>(thingUids);
    this.propertyNames = new ArrayList<>(propertyNames);
    this.operator = operator;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
  }

  public List<String> getThingUids() {
    return thingUids;
  }

  public List<String> getPropertyNames() {
    return propertyNames;
  }

  public OperatorEntity getOperator() {
    return operator;
  }

  public Double getValue() {
    return value;
  }

  public String getValueString() {
    return valueString;
  }

  public Boolean getValueBoolean() {
    return valueBoolean;
  }
}
