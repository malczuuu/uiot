package io.github.malczuuu.uiot.things.core;

import io.github.malczuuu.uiot.things.model.CursorPage;
import io.github.malczuuu.uiot.things.model.ThingCreateModel;
import io.github.malczuuu.uiot.things.model.ThingModel;
import io.github.malczuuu.uiot.things.model.ThingUpdateModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ThingService {

  CursorPage<ThingModel> getThings(String roomUid, int size);

  CursorPage<ThingModel> getThings(String roomUid, String cursorString)
      throws InvalidCursorException;

  ThingModel getThing(String roomUid, String thingUid) throws ThingNotFoundException;

  ThingModel requestThingCreation(String roomUid, ThingCreateModel thing);

  void createThing(String roomUid, ThingModel thing);

  ThingModel updateThing(String roomUid, String thingUid, ThingUpdateModel thing)
      throws ThingNotFoundException;

  void requestThingDeletion(String roomUid, String thingUid);

  void deleteThing(String roomUid, String thingUid);

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "thing not found")
  class ThingNotFoundException extends RuntimeException {}

  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "invalid cursor")
  class InvalidCursorException extends RuntimeException {}
}
