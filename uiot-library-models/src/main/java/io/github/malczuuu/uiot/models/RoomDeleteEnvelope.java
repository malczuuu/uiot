package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", RoomDeleteEnvelope.TYPE})
public class RoomDeleteEnvelope extends Envelope {

  public static final String TYPE = "room_delete";

  private final RoomDeleteEvent roomDeleteEvent;

  @JsonCreator
  public RoomDeleteEnvelope(@JsonProperty(TYPE) RoomDeleteEvent roomDeleteEvent) {
    super(TYPE);
    this.roomDeleteEvent = roomDeleteEvent;
  }

  @JsonProperty(TYPE)
  public RoomDeleteEvent getRoomDeleteEvent() {
    return roomDeleteEvent;
  }
}
