package io.github.malczuuu.uiot.history.core;

import java.util.List;

public interface ThingEventRepositoryExtensions {

  List<ThingEventEntity> findFirstPage(String roomId, int size);

  List<ThingEventEntity> findFirstPage(String roomId, String thingId, int size);
}
