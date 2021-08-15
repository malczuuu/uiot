package io.github.malczuuu.uiot.accounting.core;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

public class UpsertAwareAccountingRepositoryImpl implements UpsertAwareAccountingRepository {

  private final MongoOperations mongoOperations;

  public UpsertAwareAccountingRepositoryImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public void upsert(AccountingEntity accountingEntity) {
    CriteriaDefinition criteria =
        Criteria.where(AccountingEntity.UUID).is(accountingEntity.getUuid());
    Query query = Query.query(criteria);
    UpdateDefinition update =
        Update.update(AccountingEntity.ROOM_UID, accountingEntity.getRoomUid())
            .set(AccountingEntity.TYPE, accountingEntity.getType())
            .set(AccountingEntity.TAGS, accountingEntity.getTags())
            .set(AccountingEntity.START_TIME, accountingEntity.getStartTime())
            .set(AccountingEntity.END_TIME, accountingEntity.getEndTime())
            .set(AccountingEntity.VALUE, accountingEntity.getValue());
    mongoOperations.upsert(query, update, AccountingEntity.class);
  }
}
