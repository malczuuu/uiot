package io.github.malczuuu.uiot.history.core;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

public class ThingEventRepositoryExtensionsImpl implements ThingEventRepositoryExtensions {

  private final MongoOperations mongoOperations;

  public ThingEventRepositoryExtensionsImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public List<ThingEventEntity> findFirstPage(String roomId, int size) {
    CriteriaDefinition criteria = Criteria.where(ThingEventEntity.ROOM_FIELD).is(roomId);
    Query query =
        Query.query(criteria)
            .limit(size)
            .with(Sort.by(Order.desc(ThingEventEntity.ARRIVAL_TIME_FIELD)));
    return mongoOperations.find(query, ThingEventEntity.class);
  }

  @Override
  public List<ThingEventEntity> findFirstPage(String roomId, String thingId, int size) {
    CriteriaDefinition criteria =
        Criteria.where(ThingEventEntity.ROOM_FIELD)
            .is(roomId)
            .and(ThingEventEntity.THING_FIELD)
            .is(thingId);
    Query query =
        Query.query(criteria)
            .limit(size)
            .with(Sort.by(Order.desc(ThingEventEntity.ARRIVAL_TIME_FIELD)));
    return mongoOperations.find(query, ThingEventEntity.class);
  }
}
