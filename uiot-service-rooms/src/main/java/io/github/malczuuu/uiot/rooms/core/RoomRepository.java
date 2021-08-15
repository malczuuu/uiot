package io.github.malczuuu.uiot.rooms.core;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository
    extends MongoRepository<RoomEntity, ObjectId>, CursorAwareRoomRepository {

  Optional<RoomEntity> findByUid(String uid);

  boolean existsByUid(String uid);

  void deleteByUid(String uid);
}
