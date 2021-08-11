package io.github.malczuuu.uiot.models.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomDeleteEnvelope {

  public static final String TYPE = "room_delete";

  private final RoomDeleteEvent roomDeleteEvent;

  @JsonCreator
  public RoomDeleteEnvelope(@JsonProperty(TYPE) RoomDeleteEvent roomDeleteEvent) {
    this.roomDeleteEvent = roomDeleteEvent;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public RoomDeleteEvent getRoomDeleteEvent() {
    return roomDeleteEvent;
  }
}
