package io.github.malczuuu.uiot.things.core;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThingRepository
    extends CursorAwareThingRepository, MongoRepository<ThingEntity, ObjectId> {

  Optional<ThingEntity> findByRoomAndUid(String room, String uid);

  void deleteByRoomAndUid(String room, String uid);
}
