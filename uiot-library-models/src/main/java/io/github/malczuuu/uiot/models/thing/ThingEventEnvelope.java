package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThingEventEnvelope {

  private final List<ThingEvent> thingEvents;

  @JsonCreator
  public ThingEventEnvelope(@JsonProperty("thing_events") List<ThingEvent> thingEvents) {
    this.thingEvents = thingEvents != null ? new ArrayList<>(thingEvents) : null;
  }

  @JsonProperty("type")
  public String getType() {
    return "thing_events";
  }

  @JsonProperty("thing_events")
  public List<ThingEvent> getThingEvents() {
    return thingEvents != null ? Collections.unmodifiableList(thingEvents) : null;
  }

  @Override
  public String toString() {
    return thingEvents.toString();
  }
}
