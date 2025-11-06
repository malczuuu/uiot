package io.github.malczuuu.uiot.accounting.core;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountingRepository
    extends UpsertAwareAccountingRepository, MongoRepository<AccountingEntity, ObjectId> {

  Page<AccountingEntity> findAllByEndTimeBetweenOrderByStartTimeDesc(
      long endTimeAfter, long endTimeBefore, Pageable pageable);

  Page<AccountingEntity> findAllByRoomUidAndEndTimeBetweenOrderByStartTimeDesc(
      String roomUid, long endTimeAfter, long endTimeBefore, Pageable pageable);
}
