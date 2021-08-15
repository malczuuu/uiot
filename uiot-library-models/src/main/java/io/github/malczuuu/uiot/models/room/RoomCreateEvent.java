package io.github.malczuuu.uiot.models.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomCreateEvent {

  private final String roomUid;
  private final String roomName;
  private final Long time;

  @JsonCreator
  public RoomCreateEvent(
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("room_name") String roomName,
      @JsonProperty("time") Long time) {
    this.roomUid = roomUid;
    this.roomName = roomName;
    this.time = time;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("room_name")
  public String getRoomName() {
    return roomName;
  }

  @JsonProperty("time")
  public Long getTime() {
    return time;
  }
}
