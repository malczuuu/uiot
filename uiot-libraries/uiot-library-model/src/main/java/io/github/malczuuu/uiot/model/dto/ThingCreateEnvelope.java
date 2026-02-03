package io.github.malczuuu.uiot.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", ThingCreateEnvelope.TYPE})
public class ThingCreateEnvelope extends Envelope {

  public static final String TYPE = "thing_create";

  private final ThingCreateEvent thingCreateEvent;

  @JsonCreator
  public ThingCreateEnvelope(@JsonProperty(TYPE) ThingCreateEvent thingCreateEvent) {
    super(TYPE);
    this.thingCreateEvent = thingCreateEvent;
  }

  @JsonProperty(TYPE)
  public ThingCreateEvent getCreateThingEvent() {
    return thingCreateEvent;
  }
}
