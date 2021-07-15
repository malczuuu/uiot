package io.github.malczuuu.uiot.rooms.entity;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

public class CursorAwareRoomRepositoryImpl implements CursorAwareRoomRepository {

  private final MongoOperations mongoOperations;

  public CursorAwareRoomRepositoryImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public List<RoomEntity> findWithoutCursor(int size) {
    CriteriaDefinition criteria = new Criteria();
    Query query = Query.query(criteria).limit(size).with(Sort.by(Order.asc(RoomEntity.ID_FIELD)));
    return mongoOperations.find(query, RoomEntity.class);
  }

  @Override
  public List<RoomEntity> findWithCursor(ObjectId id, int size) {
    CriteriaDefinition criteria = Criteria.where(RoomEntity.ID_FIELD).gt(id);
    Query query = Query.query(criteria).limit(size).with(Sort.by(Order.asc(RoomEntity.ID_FIELD)));
    return mongoOperations.find(query, RoomEntity.class);
  }
}
