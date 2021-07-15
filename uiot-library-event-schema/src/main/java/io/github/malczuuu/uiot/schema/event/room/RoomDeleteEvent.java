package io.github.malczuuu.uiot.schema.event.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomDeleteEvent {

  private final String roomUid;
  private final Long timestamp;

  @JsonCreator
  public RoomDeleteEvent(
      @JsonProperty("room_uid") String roomUid, @JsonProperty("timestamp") Long timestamp) {
    this.roomUid = roomUid;
    this.timestamp = timestamp;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }
}
