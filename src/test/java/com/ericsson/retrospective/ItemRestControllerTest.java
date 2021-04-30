package com.ericsson.retrospective;

import com.ericsson.retrospective.pojo.Item;
import com.ericsson.retrospective.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ItemRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemRepository repository;

    @BeforeEach
    public void init() {
        repository.deleteAll();
        Item.setAtomicInteger(new AtomicInteger(0));
    }

    @Test
    void getAll() {
        ResponseEntity<List<Item>> entity = restTemplate.exchange(
                "/api/items",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Item>>() {});
        List<Item> items = entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(0, items.size());
    }

    @Test
    void getAItem() {
        Item i = new Item("Mad-item");
        repository.save(i);
        ResponseEntity<Item> responseEntity = restTemplate.exchange(
                "/api/items/" + i.getItemId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Mad-item", responseBody.getDescription());
    }

    @Test
    void getAItem_notFound() {

        ResponseEntity<Item> responseEntity = restTemplate.exchange(
                "/api/items/1000000" ,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    void getComments() {
        Item i = new Item("Some-item");
        i.addComments("Hello");
        repository.save(i);
        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                "/api/item/" + i.getItemId() + "/comments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );
        List<String> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Hello", responseBody.get(0));
    }

    @Test
    void getComments_notFound() {

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                "/api/item/" + 100000 + "/comments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );
        List<String> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getVote() {
        Item i = new Item("Lol");
        i.setVotes(20);
        repository.save(i);
        ResponseEntity<Integer>  responseEntity = restTemplate.exchange(
                "/api/item/" + i.getItemId() + "/vote",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Integer>() {}
        );
        Integer responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(20, responseBody);
    }

    @Test
    void getVote_notFound() {

        ResponseEntity<Integer>  responseEntity = restTemplate.exchange(
                "/api/item/" + 10000000 + "/vote",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Integer>() {}
        );
        Integer responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void putVote() {
        Item i = new Item("Lol");
        i.setVotes(20);
        repository.save(i);
        ResponseEntity<Item> responseEntity = restTemplate.exchange(
                "/api/item/" + i.getItemId() + "/vote",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(21, responseBody.getVotes());
    }

    @Test
    void putVote_notFound() {

        ResponseEntity<Item> responseEntity = restTemplate.exchange(
                "/api/item/" + 100000 + "/vote",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void putComment() {
        Item i = new Item("Lol");
        repository.save(i);
        ResponseEntity<Item>  responseEntity = restTemplate.exchange(
                "/api/item/" + i.getItemId() + "/comment?comment=hello",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("hello", responseBody.getComments().get(0));
    }

    @Test
    void putComment_notFound() {
        ResponseEntity<Item>  responseEntity = restTemplate.exchange(
                "/api/item/" + 1000000 + "/comment?comment=hello",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}