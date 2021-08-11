package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.malczuuu.uiot.models.telemetry.Record;
import java.util.ArrayList;
import java.util.List;

public class ThingEvent {

  public static ThingEvent from(
      Record record, String id, String room, String thing, String property, Long arrivalTime) {
    return new ThingEvent(
        record.getName(),
        record.getTime() != null ? (long) (record.getTime() * 1000_000_000L) : arrivalTime,
        record.getValue(),
        record.getValueString(),
        record.getValueBoolean(),
        record.getValueData(),
        record.getUnit(),
        id,
        room,
        thing,
        property,
        arrivalTime);
  }

  private final String name;
  private final Long time;
  private final Double value;
  private final String valueString;
  private final Boolean valueBoolean;
  private final String valueData;
  private final String unit;

  private final String id;
  private final String room;
  private final String thing;
  private final String property;
  private final Long arrivalTime;

  @JsonCreator
  public ThingEvent(
      @JsonProperty("n") String name,
      @JsonProperty("t") Long time,
      @JsonProperty("v") Double value,
      @JsonProperty("vs") String valueString,
      @JsonProperty("vb") Boolean valueBoolean,
      @JsonProperty("vd") String valueData,
      @JsonProperty("u") String unit,
      @JsonProperty("id") String id,
      @JsonProperty("ro") String room,
      @JsonProperty("th") String thing,
      @JsonProperty("pr") String property,
      @JsonProperty("at") Long arrivalTime) {
    this.name = name;
    this.time = time;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
    this.valueData = valueData;
    this.unit = unit;
    this.id = id;
    this.room = room;
    this.thing = thing;
    this.property = property;
    this.arrivalTime = arrivalTime;
  }

  @JsonProperty("n")
  public String getName() {
    return name;
  }

  @JsonProperty("t")
  public Long getTime() {
    return time;
  }

  @JsonProperty("v")
  public Double getValue() {
    return value;
  }

  @JsonProperty("vs")
  public String getValueString() {
    return valueString;
  }

  @JsonProperty("vb")
  public Boolean getValueBoolean() {
    return valueBoolean;
  }

  @JsonProperty("vd")
  public String getValueData() {
    return valueData;
  }

  @JsonProperty("u")
  public String getUnit() {
    return unit;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("ro")
  public String getRoom() {
    return room;
  }

  @JsonProperty("th")
  public String getThing() {
    return thing;
  }

  @JsonProperty("pr")
  public String getProperty() {
    return property;
  }

  @JsonProperty("at")
  public Long getArrivalTime() {
    return arrivalTime;
  }

  @Override
  public String toString() {
    List<String> lines = new ArrayList<>();

    if (getName() != null) lines.add("n=" + getName());
    if (getTime() != null) lines.add("t=" + getTime());
    if (getValue() != null) lines.add("v=" + getValue());
    if (getValueString() != null) lines.add("vs=" + getValueString());
    if (getValueBoolean() != null) lines.add("vb=" + getValueBoolean());
    if (getValueData() != null) lines.add("vd=" + getValueData());
    if (getUnit() != null) lines.add("u=" + getUnit());
    if (getId() != null) lines.add("id=" + getId());
    if (getRoom() != null) lines.add("ro=" + getRoom());
    if (getThing() != null) lines.add("th=" + getThing());
    if (getProperty() != null) lines.add("pr=" + getProperty());
    if (getArrivalTime() != null) lines.add("at=" + getArrivalTime().toString());

    return "DeviceEvent{ " + String.join(", ", lines) + " }";
  }
}
