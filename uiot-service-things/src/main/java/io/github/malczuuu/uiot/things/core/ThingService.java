package io.github.malczuuu.uiot.things.core;

import io.github.malczuuu.uiot.problems.InvalidCursorException;
import io.github.malczuuu.uiot.things.model.CursorPage;
import io.github.malczuuu.uiot.things.model.ThingCreateModel;
import io.github.malczuuu.uiot.things.model.ThingModel;
import io.github.malczuuu.uiot.things.model.ThingUpdateModel;

public interface ThingService {

  CursorPage<ThingModel> getThings(String roomUid, int size);

  CursorPage<ThingModel> getThings(String roomUid, String cursorString)
      throws InvalidCursorException;

  ThingModel getThing(String roomUid, String thingUid) throws ThingNotFoundException;

  ThingModel createThing(String roomUid, ThingCreateModel thing);

  ThingModel updateThing(String roomUid, String thingUid, ThingUpdateModel thing)
      throws ThingNotFoundException;

  void deleteThings(String roomUid);

  void deleteThing(String roomUid, String thingUid);
}
