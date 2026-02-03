package io.github.malczuuu.uiot.room.core;

import java.util.List;
import org.bson.types.ObjectId;

public interface CursorAwareRoomRepository {

  List<RoomEntity> findWithoutCursor(int size);

  List<RoomEntity> findWithCursor(ObjectId id, int size);
}
