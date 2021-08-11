package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThingDeleteEnvelope {

  public static final String TYPE = "thing_delete";

  private final ThingDeleteEvent thingDeleteEvent;

  @JsonCreator
  public ThingDeleteEnvelope(@JsonProperty(TYPE) ThingDeleteEvent thingDeleteEvent) {
    this.thingDeleteEvent = thingDeleteEvent;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public ThingDeleteEvent getDeleteThingEvent() {
    return thingDeleteEvent;
  }
}
