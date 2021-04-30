package com.ericsson.retrospective;

import com.ericsson.retrospective.pojo.Category;
import com.ericsson.retrospective.pojo.Item;
import com.ericsson.retrospective.pojo.Team;
import com.ericsson.retrospective.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestItem {

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void init() {
        itemRepository.deleteAll();
        Team.setAtomicInteger(new AtomicInteger(0));
    }

    @Test
    void testEnumOne() {
        Item item = new Item(Category.MAD, "Weird errors");
        for (int i = 0; i < 2; i++) {
            item.setVotes(1);
        }
        assertEquals(Category.MAD, item.getCategory());
    }

    @Test
    void testEnumTwo() {
        Item item = new Item(Category.GLAD, "Great testing");
        for (int i = 0; i < 3; i++) {
            item.setVotes(1);
        }
        assertEquals(Category.GLAD, item.getCategory());
    }

    @Test
    void testEnumThree() {
        Item item = new Item(Category.SAD, "Empty story description");
        for (int i = 0; i < 3; i++) {
            item.setVotes(1);
        }
        assertEquals(Category.SAD, item.getCategory());
    }

    @Test
    void testSetCategory() {
        Item item = new Item(Category.SAD, "some words");
        item.setCategory(Category.GLAD);
        assertEquals(Category.GLAD, item.getCategory());
    }

    @Test
    void testDescription() {
        Item item = new Item(Category.GLAD, "some words");
        item.setDescription("descriptions");
        assertEquals("descriptions", item.getDescription());
    }

    @Test
    void testGetVotes() {
        Item item = new Item(Category.SAD, "some words");
        item.setVotes(1);
        assertEquals(1, item.getVotes());
    }

    @Test
    void testToString() {
        Item item = new Item(Category.GLAD, "Great security");
        item.setVotes(1);
        item.addComments("Generic comment");
        assertEquals("Item [votes=1, Category=GLAD, description=Great security," +
                " comments=[Generic comment]]", item.toString());
    }

    @Test
    void testGetCommnets() {
        Item item = new Item(Category.GLAD, "some words");
        item.addComments("some comments");
        assertEquals(Arrays.asList("some comments"), item.getComments());
    }

    @Test
    void testGetItemId(){
        Item item = new Item(Category.GLAD, "some words");
        item.setItemId(3);
        assertEquals(3, item.getItemId());
    }

}
