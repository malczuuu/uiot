package io.github.malczuuu.uiot.rules.core;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleRepository
    extends MongoRepository<RuleEntity, ObjectId>, SearchingRuleRepository {}
