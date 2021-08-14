package io.github.malczuuu.uiot.accounting.stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricAggregate {

  private final String uuid;
  private final Double value;

  @JsonCreator
  public MetricAggregate(@JsonProperty("uuid") String uuid, @JsonProperty("value") Double value) {
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
  public MetricAggregate aggregate(double value) {
    return new MetricAggregate(getUuid(), getValue() + value);
  }
}
