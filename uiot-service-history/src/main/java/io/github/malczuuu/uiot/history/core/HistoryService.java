package io.github.malczuuu.uiot.history.core;

import io.github.malczuuu.uiot.history.model.EventHistory;
import io.github.malczuuu.uiot.schema.event.thing.ThingEvent;

public interface HistoryService {

  EventHistory getEventHistory(String roomUid, int size);

  EventHistory getThingEventHistory(String roomUid, String thingUid, int size);

  void storeEvent(ThingEvent event);
}
