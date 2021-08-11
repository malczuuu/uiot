package io.github.malczuuu.uiot.models.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomCreateEvent {

  private final String roomUid;
  private final String roomName;
  private final Long timestamp;

  @JsonCreator
  public RoomCreateEvent(
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("room_name") String roomName,
      @JsonProperty("timestamp") Long timestamp) {
    this.roomUid = roomUid;
    this.roomName = roomName;
    this.timestamp = timestamp;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("room_name")
  public String getRoomName() {
    return roomName;
  }

  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }
}
