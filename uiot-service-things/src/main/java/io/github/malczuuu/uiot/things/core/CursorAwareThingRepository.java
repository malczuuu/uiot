package io.github.malczuuu.uiot.things.core;

import java.util.List;
import org.bson.types.ObjectId;

public interface CursorAwareThingRepository {

  List<ThingEntity> findWithoutCursor(String roomUid, int size);

  List<ThingEntity> findWithCursor(String roomUid, ObjectId id, int size);
}
