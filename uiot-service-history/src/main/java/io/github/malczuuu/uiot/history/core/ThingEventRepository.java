package io.github.malczuuu.uiot.history.core;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThingEventRepository
    extends ThingEventRepositoryExtensions, MongoRepository<ThingEventEntity, ObjectId> {

  void deleteAllByThingAndIdLessThanEqual(String thing, ObjectId id);
}
