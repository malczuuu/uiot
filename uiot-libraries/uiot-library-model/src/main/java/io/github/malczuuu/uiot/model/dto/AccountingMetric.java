package io.github.malczuuu.uiot.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class AccountingMetric {

  private final String uuid;
  private final String type;
  private final String roomUid;
  private final Double value;
  private final Long time;
  private final Map<String, String> tags;

  @JsonCreator
  public AccountingMetric(
      @JsonProperty("uuid") String uuid,
      @JsonProperty("type") String type,
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("value") Double value,
      @JsonProperty("time") Long time,
      @JsonProperty("tags") Map<String, String> tags) {
    this.uuid = uuid;
    this.type = type;
    this.roomUid = roomUid;
    this.value = value;
    this.time = time;
    this.tags = tags;
  }

  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
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
  public Long getTime() {
    return time;
  }

  @JsonProperty("tags")
  public Map<String, String> getTags() {
    return tags;
  }
}
