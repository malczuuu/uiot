package io.github.malczuuu.uiot.accounting.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Map;

public class AccountingModel {

  private final String roomUid;
  private final String type;
  private final Double value;
  private final Double startTime;
  private final String startTimeIso;
  private final Double endTime;
  private final String endTimeIso;
  private final Map<String, String> tags;

  @JsonCreator
  public AccountingModel(
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("type") String type,
      @JsonProperty("value") Double value,
      @JsonProperty("start_time") Double startTime,
      @JsonProperty("start_time_iso") String startTimeIso,
      @JsonProperty("end_time") Double endTime,
      @JsonProperty("end_time_iso") String endTimeIso,
      @JsonProperty("tags") Map<String, String> tags) {
    this.roomUid = roomUid;
    this.type = type;
    this.value = value;
    this.startTime = startTime;
    this.startTimeIso = startTimeIso;
    this.endTime = endTime;
    this.endTimeIso = endTimeIso;
    this.tags = tags;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("value")
  public Double getValue() {
    return value;
  }

  @JsonProperty("start_time")
  public Double getStartTime() {
    return startTime;
  }

  @JsonProperty("start_time_iso")
  public String getStartTimeIso() {
    return startTimeIso;
  }

  @JsonProperty("end_time")
  public Double getEndTime() {
    return endTime;
  }

  @JsonProperty("end_time_iso")
  public String getEndTimeIso() {
    return endTimeIso;
  }

  @JsonProperty("tags")
  public Map<String, String> getTags() {
    return Collections.unmodifiableMap(tags);
  }
}
