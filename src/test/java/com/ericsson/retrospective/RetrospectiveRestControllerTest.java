package com.ericsson.retrospective;


import com.ericsson.retrospective.pojo.Category;
import com.ericsson.retrospective.pojo.Item;
import com.ericsson.retrospective.pojo.Retrospective;
import com.ericsson.retrospective.repository.ItemRepository;
import com.ericsson.retrospective.repository.RetrospectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RetrospectiveRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RetrospectiveRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void init(){
        repository.deleteAll();
        Retrospective.setAtomicInteger(new AtomicInteger(0));
    }

    @Test
    void getAll() {
        ResponseEntity<List<Retrospective>> entity = restTemplate.exchange(
                "/api/retrospectives",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Retrospective>>() {}
        );
        List<Retrospective> retrospectives= entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(0, retrospectives.size());
    }

    @Test
    void deleteAll() {
        Retrospective retrospective = new Retrospective("SprintAlpha");
        repository.save(retrospective);
        restTemplate.delete("/api/delRetrospectives");
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void addRetrospective() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("name","TestRetrospective");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Retrospective> responseEntity = restTemplate.postForEntity(
                "/api/addRetrospective",request, Retrospective.class
        );
        Retrospective responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("TestRetrospective", responseBody.getName());

    }

    @Test
    void getRetrospective() {

        Retrospective r = new Retrospective("Retro-PRO");
        repository.save(r);
        ResponseEntity<Retrospective> responseEntity = restTemplate.exchange(
                "/api/retrospectives/" + r.getRetrospectiveId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Retrospective>() {}
        );
        Retrospective responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Retro-PRO", responseBody.getName());

    }

    @Test
    void getRetrospective_notFound() {


        ResponseEntity<Retrospective> responseEntity = restTemplate.exchange(
                "/api/retrospectives/" + 1000000,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Retrospective>() {}
        );
        Retrospective responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    void getRetrospectiveItems() {
        Retrospective retrospective = new Retrospective("SprintAlpha");
        Item i = new Item(Category.GLAD, "4");
        Item i2 = new Item(Category.GLAD, "5");
        itemRepository.save(i);
        itemRepository.save(i2);
        retrospective.addItem(i.getItemId());
        retrospective.addItem(i2.getItemId());
        repository.save(retrospective);
        ResponseEntity<List<Item>> responseEntity = restTemplate.exchange(
                "/api/retrospectiveItems/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Item>>() {}
        );
        List<Item> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseBody.size());

    }

    @Test
    void getRetrospectiveItems_notFound() {

        ResponseEntity<List<Item>> responseEntity = restTemplate.exchange(
                "/api/retrospectiveItems/100000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Item>>() {}
        );
        List<Item> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    void addSprintItem() {
        Retrospective retrospective = new Retrospective("SprintAlpha");
        retrospective.addItem(4);
        retrospective.addItem(5);
        repository.save(retrospective);
        ResponseEntity<Item> responseEntity = restTemplate.exchange(
                "/api/addSprintItem/1?mood=GLAD&itemDesc=HARD testing here",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Category.GLAD, responseBody.getCategory());
    }

    @Test
    void addSprintItem_notFound() {

        ResponseEntity<Item> responseEntity = restTemplate.exchange(
                "/api/addSprintItem/100000?mood=GLAD&itemDesc=HARD testing here",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Item>() {}
        );
        Item responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    

}