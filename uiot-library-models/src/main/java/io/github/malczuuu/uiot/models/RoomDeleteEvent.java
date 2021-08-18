package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomDeleteEvent {

  private final String roomUid;
  private final Long time;

  @JsonCreator
  public RoomDeleteEvent(
      @JsonProperty("room_uid") String roomUid, @JsonProperty("time") Long time) {
    this.roomUid = roomUid;
    this.time = time;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("time")
  public Long getTime() {
    return time;
  }
}
