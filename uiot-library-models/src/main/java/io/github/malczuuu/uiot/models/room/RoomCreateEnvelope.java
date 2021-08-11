package io.github.malczuuu.uiot.models.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomCreateEnvelope {

  public static final String TYPE = "room_create";

  private final RoomCreateEvent roomCreateEvent;

  @JsonCreator
  public RoomCreateEnvelope(@JsonProperty(TYPE) RoomCreateEvent roomCreateEvent) {
    this.roomCreateEvent = roomCreateEvent;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public RoomCreateEvent getRoomCreateEvent() {
    return roomCreateEvent;
  }
}
