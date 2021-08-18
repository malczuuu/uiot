package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.malczuuu.uiot.models.TypedEnvelope;

@JsonPropertyOrder({"type", ThingCreateEnvelope.TYPE})
public class ThingCreateEnvelope extends TypedEnvelope {

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
