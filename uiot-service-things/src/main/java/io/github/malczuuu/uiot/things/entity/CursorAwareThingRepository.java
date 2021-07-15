package io.github.malczuuu.uiot.things.entity;

import java.util.List;
import org.bson.types.ObjectId;

public interface CursorAwareThingRepository {

  List<ThingEntity> findWithoutCursor(String room, int size);

  List<ThingEntity> findWithCursor(String room, ObjectId id, int size);
}
