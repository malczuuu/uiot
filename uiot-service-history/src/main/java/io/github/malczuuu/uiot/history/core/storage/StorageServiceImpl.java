package io.github.malczuuu.uiot.history.core.storage;

import io.github.malczuuu.uiot.history.core.ThingEventEntity;
import io.github.malczuuu.uiot.models.room.RoomCreateEvent;
import io.github.malczuuu.uiot.models.room.RoomDeleteEvent;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {

  private final StorageNameService storageNameService;
  private final MongoOperations mongoOperations;
  private final StorageRepository storageRepository;

  public StorageServiceImpl(
      StorageNameService storageNameService,
      MongoOperations mongoOperations,
      StorageRepository storageRepository) {
    this.storageNameService = storageNameService;
    this.mongoOperations = mongoOperations;
    this.storageRepository = storageRepository;
  }

  @Override
  public void createStorage(RoomCreateEvent roomCreate) {
    String storageName = storageNameService.getStorageName(roomCreate.getRoomUid());

    storageRepository.save(new StorageEntity(roomCreate.getRoomUid(), storageName));

    mongoOperations.createCollection(storageName);

    IndexOperations indexOperations = mongoOperations.indexOps(storageName);
    indexOperations.ensureIndex(
        new Index()
            .on(ThingEventEntity.ROOM_UID, Direction.ASC)
            .on(ThingEventEntity.ARRIVAL_TIME_FIELD, Direction.ASC));
    indexOperations.ensureIndex(
        new Index()
            .on(ThingEventEntity.ROOM_UID, Direction.ASC)
            .on(ThingEventEntity.THING_UID_FIELD, Direction.ASC)
            .on(ThingEventEntity.ARRIVAL_TIME_FIELD, Direction.ASC));
  }

  @Override
  public void deleteStorage(RoomDeleteEvent roomDelete) {
    String storageName = storageNameService.getStorageName(roomDelete.getRoomUid());
    mongoOperations.dropCollection(storageName);
    storageRepository.deleteByRoomUid(roomDelete.getRoomUid());
  }
}
