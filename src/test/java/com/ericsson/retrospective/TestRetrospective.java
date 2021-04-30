package com.ericsson.retrospective;

import com.ericsson.retrospective.pojo.Category;
import com.ericsson.retrospective.pojo.Item;
import com.ericsson.retrospective.pojo.Retrospective;
import com.ericsson.retrospective.pojo.Team;
import com.ericsson.retrospective.repository.RetrospectiveRepository;
import com.ericsson.retrospective.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestRetrospective {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    RetrospectiveRepository retrospectiveRepository;

    @BeforeEach
    void init() {
        teamRepository.deleteAll();
        Team.setAtomicInteger(new AtomicInteger(0));
        retrospectiveRepository.deleteAll();
        Retrospective.setAtomicInteger(new AtomicInteger(0));


    }

    @Test
    void testAddItem() {
        Item item1 = new Item(Category.MAD, "No tests");
        Retrospective retrospective = new Retrospective("Alpha");
        retrospective.addItem(item1.getItemId());
        assertEquals("No tests", item1.getDescription());
    }

    @Test
    void testGetRetrospectiveId() {
        Retrospective retrospective = new Retrospective("Alpha");
        retrospective.setRetrospectiveId(2);
        assertEquals(2, retrospective.getRetrospectiveId());
    }

    @Test
    void testGetName() {
        Retrospective retrospective = new Retrospective("Test");
        retrospective.setName("John");
        assertEquals("John", retrospective.getName());
    }

    @Test
    void testGetItems() {
        Retrospective retrospective = new Retrospective("Test" );
        List<Long> items = new ArrayList<Long>(Arrays.asList(1L, 2L, 3L));
        retrospective.setItemIdList(items);
        assertEquals(Arrays.asList(1L, 2L, 3L), retrospective.getItemIdList());
    }
}
