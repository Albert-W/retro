package com.ericsson.retrospective.repository;

import com.ericsson.retrospective.pojo.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, Long> {

}
