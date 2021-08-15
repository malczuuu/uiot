package io.github.malczuuu.uiot.connectivity.core;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConnectivityRepository extends MongoRepository<ConnectivityEntity, ObjectId> {

  Optional<ConnectivityEntity> findByRoomUidAndThingUid(String roomUid, String thingUid);

  void deleteByRoomUidAndThingUid(String roomUid, String thingUid);
}
