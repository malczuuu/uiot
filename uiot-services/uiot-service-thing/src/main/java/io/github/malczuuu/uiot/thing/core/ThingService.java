package io.github.malczuuu.uiot.thing.core;

import io.github.malczuuu.uiot.model.dto.Pagination;
import io.github.malczuuu.uiot.model.error.InvalidCursorException;
import io.github.malczuuu.uiot.thing.model.CursorPage;
import io.github.malczuuu.uiot.thing.model.ThingCreateModel;
import io.github.malczuuu.uiot.thing.model.ThingModel;
import io.github.malczuuu.uiot.thing.model.ThingUpdateModel;

public interface ThingService {

  CursorPage<ThingModel> getThings(String roomUid, Pagination pagination);

  CursorPage<ThingModel> getThings(String roomUid, String cursorString)
      throws InvalidCursorException;

  ThingModel getThing(String roomUid, String thingUid) throws ThingNotFoundException;

  ThingModel createThing(String roomUid, ThingCreateModel thing);

  ThingModel updateThing(String roomUid, String thingUid, ThingUpdateModel thing)
      throws ThingNotFoundException;

  void deleteThings(String roomUid);

  void deleteThing(String roomUid, String thingUid);
}
