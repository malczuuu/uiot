package io.github.malczuuu.uiot.history.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryRecord {

  private final String name;
  private final Double time;
  private final String timeIso;
  private final Double value;
  private final String valueString;
  private final Boolean valueBoolean;
  private final String valueData;
  private final String unit;

  private final String roomUid;
  private final String thingUid;
  private final String propertyName;
  private final Double arrivalTime;
  private final String arrivalTimeIso;

  @JsonCreator
  public HistoryRecord(
      @JsonProperty("name") String name,
      @JsonProperty("time") Double time,
      @JsonProperty("time_iso") String timeIso,
      @JsonProperty("value") Double value,
      @JsonProperty("value_string") String valueString,
      @JsonProperty("value_boolean") Boolean valueBoolean,
      @JsonProperty("value_data") String valueData,
      @JsonProperty("unit") String unit,
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("thing_uid") String thingUid,
      @JsonProperty("property_name") String propertyName,
      @JsonProperty("arrival_time") Double arrivalTime,
      @JsonProperty("arrival_time_iso") String arrivalTimeIso) {
    this.name = name;
    this.time = time;
    this.timeIso = timeIso;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
    this.valueData = valueData;
    this.unit = unit;
    this.roomUid = roomUid;
    this.thingUid = thingUid;
    this.propertyName = propertyName;
    this.arrivalTime = arrivalTime;
    this.arrivalTimeIso = arrivalTimeIso;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("time")
  public Double getTime() {
    return time;
  }

  @JsonProperty("time_iso")
  public String getTimeIso() {
    return timeIso;
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

  @JsonProperty("value_data")
  public String getValueData() {
    return valueData;
  }

  @JsonProperty("unit")
  public String getUnit() {
    return unit;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("thing_uid")
  public String getThingUid() {
    return thingUid;
  }

  @JsonProperty("property_name")
  public String getPropertyName() {
    return propertyName;
  }

  @JsonProperty("arrival_time")
  public Double getArrivalTime() {
    return arrivalTime;
  }

  @JsonProperty("arrival_time_iso")
  public String getArrivalTimeIso() {
    return arrivalTimeIso;
  }
}
