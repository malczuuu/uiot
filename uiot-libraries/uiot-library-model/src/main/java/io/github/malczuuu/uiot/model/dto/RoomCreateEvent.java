package io.github.malczuuu.uiot.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomCreateEvent {

  private final String roomUid;
  private final String roomName;

  @JsonCreator
  public RoomCreateEvent(
      @JsonProperty("room_uid") String roomUid, @JsonProperty("room_name") String roomName) {
    this.roomUid = roomUid;
    this.roomName = roomName;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("room_name")
  public String getRoomName() {
    return roomName;
  }
}
