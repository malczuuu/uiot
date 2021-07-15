package io.github.malczuuu.uiot.things.core;

import io.github.malczuuu.uiot.schema.event.thing.ThingCreateEvent;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEvent;

public interface ThingEventBroker {

  void publish(ThingDeleteEvent event);

  void publish(ThingCreateEvent event);
}
