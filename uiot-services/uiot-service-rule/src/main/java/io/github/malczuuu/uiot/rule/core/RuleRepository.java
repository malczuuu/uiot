package io.github.malczuuu.uiot.rule.core;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleRepository
    extends MongoRepository<RuleEntity, ObjectId>, SearchingRuleRepository {

  Page<RuleEntity> findAllByRoomUid(String roomUid, Pageable pageable);

  Optional<RuleEntity> findByRoomUidAndUid(String roomUid, String uid);

  void deleteByRoomUid(String roomUid);

  void deleteByRoomUidAndUid(String roomUid, String uid);
}
