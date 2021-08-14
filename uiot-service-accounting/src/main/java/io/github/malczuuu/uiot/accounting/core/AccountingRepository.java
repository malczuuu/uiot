package io.github.malczuuu.uiot.accounting.core;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountingRepository extends MongoRepository<AccountingEntity, ObjectId> {}
