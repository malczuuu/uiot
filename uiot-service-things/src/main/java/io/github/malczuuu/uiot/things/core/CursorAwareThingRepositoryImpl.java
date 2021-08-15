package io.github.malczuuu.uiot.things.core;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

public class CursorAwareThingRepositoryImpl implements CursorAwareThingRepository {

  private final MongoOperations mongoOperations;

  public CursorAwareThingRepositoryImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public List<ThingEntity> findWithoutCursor(String room, int size) {
    CriteriaDefinition criteria = Criteria.where(ThingEntity.ROOM_FIELD).is(room);
    Query query = Query.query(criteria).limit(size).with(Sort.by(Order.asc(ThingEntity.ID_FIELD)));
    return mongoOperations.find(query, ThingEntity.class);
  }

  @Override
  public List<ThingEntity> findWithCursor(String room, ObjectId id, int size) {
    CriteriaDefinition criteria =
        Criteria.where(ThingEntity.ROOM_FIELD).is(room).and(ThingEntity.ID_FIELD).gt(id);
    Query query = Query.query(criteria).limit(size).with(Sort.by(Order.asc(ThingEntity.ID_FIELD)));
    return mongoOperations.find(query, ThingEntity.class);
  }
}
