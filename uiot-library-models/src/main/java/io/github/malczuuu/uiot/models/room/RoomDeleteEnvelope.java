package io.github.malczuuu.uiot.models.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.malczuuu.uiot.models.TypedEnvelope;

@JsonPropertyOrder({"type", RoomDeleteEnvelope.TYPE})
public class RoomDeleteEnvelope extends TypedEnvelope {

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
