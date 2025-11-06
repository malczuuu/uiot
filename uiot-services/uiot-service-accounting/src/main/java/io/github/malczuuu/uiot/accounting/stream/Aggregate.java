package io.github.malczuuu.uiot.accounting.stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Aggregate {

  private final String uuid;
  private final Double value;

  @JsonCreator
  public Aggregate(@JsonProperty("uuid") String uuid, @JsonProperty("value") Double value) {
    this.uuid = uuid;
    this.value = value;
  }

  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  @JsonProperty("value")
  public Double getValue() {
    return value;
  }

  @JsonIgnore
  public Aggregate aggregate(double value) {
    return new Aggregate(getUuid(), getValue() + value);
  }
}
