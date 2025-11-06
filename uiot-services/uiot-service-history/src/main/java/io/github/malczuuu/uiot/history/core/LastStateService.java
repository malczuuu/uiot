package io.github.malczuuu.uiot.history.core;

import io.github.malczuuu.uiot.history.model.EventHistory;

public interface LastStateService {

  EventHistory getLastState(String roomUid, String thingUid, String propertyName);

  EventHistory getLastState(String roomUid, String thingUid);
}
