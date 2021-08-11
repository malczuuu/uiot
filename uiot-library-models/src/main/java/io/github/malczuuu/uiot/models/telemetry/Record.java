package io.github.malczuuu.uiot.models.telemetry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Record {

  private final String name;
  private final Double value;
  private final String valueString;
  private final Boolean valueBoolean;
  private final String valueData;
  private final String unit;
  private final Double time;

  @JsonCreator
  public Record(
      @JsonProperty("n") String name,
      @JsonProperty("v") Double value,
      @JsonProperty("vs") String valueString,
      @JsonProperty("vb") Boolean valueBoolean,
      @JsonProperty("vd") String valueData,
      @JsonProperty("u") String unit,
      @JsonProperty("t") Double time) {
    this.name = name;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
    this.valueData = valueData;
    this.unit = unit;
    this.time = time;
  }

  @JsonProperty("n")
  public String getName() {
    return name;
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

  @JsonProperty("t")
  public Double getTime() {
    return time;
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

    return "Record{ " + String.join(", ", lines) + " }";
  }
}
