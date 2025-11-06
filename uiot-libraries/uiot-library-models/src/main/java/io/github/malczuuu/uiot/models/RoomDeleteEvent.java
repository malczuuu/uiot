package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomDeleteEvent {

  private final String roomUid;

  @JsonCreator
  public RoomDeleteEvent(@JsonProperty("room_uid") String roomUid) {
    this.roomUid = roomUid;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }
}
