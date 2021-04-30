package com.ericsson.retrospective.repository;

import com.ericsson.retrospective.pojo.Team;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TeamRepository extends MongoRepository<Team, Long> {


}
