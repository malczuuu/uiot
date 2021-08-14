package io.github.malczuuu.uiot.models.accounting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class MetricModel {

  private final String type;
  private final String roomUid;
  private final Double value;
  private final Double time;
  private final Map<String, String> tags;

  @JsonCreator
  public MetricModel(
      @JsonProperty("type") String type,
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("value") Double value,
      @JsonProperty("time") Double time,
      @JsonProperty("tags") Map<String, String> tags) {
    this.type = type;
    this.roomUid = roomUid;
    this.value = value;
    this.time = time;
    this.tags = tags;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("value")
  public Double getValue() {
    return value;
  }

  @JsonProperty("time")
  public Double getTime() {
    return time;
  }

  @JsonProperty("tags")
  public Map<String, String> getTags() {
    return tags;
  }
}
