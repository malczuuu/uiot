package io.github.malczuuu.uiot.history.core;

import io.github.malczuuu.uiot.history.core.storage.StorageNameService;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ThingEventRepositoryImpl implements ThingEventRepository {

  private final StorageNameService storageNameService;
  private final MongoOperations mongoOperations;

  public ThingEventRepositoryImpl(
      StorageNameService storageNameService, MongoOperations mongoOperations) {
    this.storageNameService = storageNameService;
    this.mongoOperations = mongoOperations;
  }

  @Override
  public ThingEventEntity save(ThingEventEntity event) {
    String roomId = event.getRoomUid();
    String storageName = storageNameService.getStorageName(roomId);
    return mongoOperations.save(event, storageName);
  }

  @Override
  public List<ThingEventEntity> findFirstPage(String roomId, int size) {
    String storageName = storageNameService.getStorageName(roomId);
    CriteriaDefinition criteria = new Criteria();
    Query query =
        Query.query(criteria)
            .limit(size)
            .with(Sort.by(Order.desc(ThingEventEntity.ARRIVAL_TIME_FIELD)));
    return mongoOperations.find(query, ThingEventEntity.class, storageName);
  }

  @Override
  public List<ThingEventEntity> findFirstPage(String roomId, String thingId, int size) {
    String storageName = storageNameService.getStorageName(roomId);
    CriteriaDefinition criteria = Criteria.where(ThingEventEntity.THING_UID_FIELD).is(thingId);
    Query query =
        Query.query(criteria)
            .limit(size)
            .with(Sort.by(Order.desc(ThingEventEntity.ARRIVAL_TIME_FIELD)));
    return mongoOperations.find(query, ThingEventEntity.class, storageName);
  }
}
