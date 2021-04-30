package com.ericsson.retrospective.repository;

import com.ericsson.retrospective.pojo.Retrospective;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RetrospectiveRepository extends MongoRepository<Retrospective, Long> {
}
