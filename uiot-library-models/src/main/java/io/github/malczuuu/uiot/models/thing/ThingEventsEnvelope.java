package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.malczuuu.uiot.models.TypedEnvelope;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonPropertyOrder({"type", ThingEventsEnvelope.TYPE})
public class ThingEventsEnvelope extends TypedEnvelope {

  public static final String TYPE = "thing_events";

  private final List<ThingEvent> thingEvents;

  @JsonCreator
  public ThingEventsEnvelope(@JsonProperty(TYPE) List<ThingEvent> thingEvents) {
    super(TYPE);
    this.thingEvents = thingEvents != null ? new ArrayList<>(thingEvents) : null;
  }

  @JsonProperty(TYPE)
  public List<ThingEvent> getThingEvents() {
    return thingEvents != null ? Collections.unmodifiableList(thingEvents) : null;
  }

  @Override
  public String toString() {
    return thingEvents.toString();
  }
}
