package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThingCreateEnvelope {

  public static final String TYPE = "thing_create";

  private final ThingCreateEvent thingCreateEvent;

  @JsonCreator
  public ThingCreateEnvelope(@JsonProperty(TYPE) ThingCreateEvent thingCreateEvent) {
    this.thingCreateEvent = thingCreateEvent;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public ThingCreateEvent getCreateThingEvent() {
    return thingCreateEvent;
  }
}
