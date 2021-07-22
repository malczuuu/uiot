package io.github.malczuuu.uiot.history.core.storage;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageRepository extends MongoRepository<StorageEntity, ObjectId> {

  void deleteByRoomUid(String roomUid);
}
