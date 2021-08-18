package io.github.malczuuu.uiot.models.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.malczuuu.uiot.models.TypedEnvelope;

@JsonPropertyOrder({"type", RoomCreateEnvelope.TYPE})
public class RoomCreateEnvelope extends TypedEnvelope {

  public static final String TYPE = "room_create";

  private final RoomCreateEvent roomCreateEvent;

  @JsonCreator
  public RoomCreateEnvelope(@JsonProperty(TYPE) RoomCreateEvent roomCreateEvent) {
    super(TYPE);
    this.roomCreateEvent = roomCreateEvent;
  }

  @JsonProperty(TYPE)
  public RoomCreateEvent getRoomCreateEvent() {
    return roomCreateEvent;
  }
}
