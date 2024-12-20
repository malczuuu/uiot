package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class AccountingWindow {

  private final String uuid;
  private final String type;
  private final String roomUid;
  private final Double value;
  private final List<Long> times;
  private final Map<String, String> tags;

  @JsonCreator
  public AccountingWindow(
      @JsonProperty("uuid") String uuid,
      @JsonProperty("type") String type,
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("value") Double value,
      @JsonProperty("times") List<Long> times,
      @JsonProperty("tags") Map<String, String> tags) {
    this.uuid = uuid;
    this.type = type;
    this.roomUid = roomUid;
    this.value = value;
    this.times = times;
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

  @JsonProperty("times")
  public List<Long> getTimes() {
    return times;
  }

  @JsonProperty("tags")
  public Map<String, String> getTags() {
    return tags;
  }
}
