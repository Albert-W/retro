package com.ericsson.retrospective.rest_controller;

import com.ericsson.retrospective.pojo.Category;
import com.ericsson.retrospective.pojo.Item;
import com.ericsson.retrospective.pojo.Retrospective;
import com.ericsson.retrospective.repository.ItemRepository;
import com.ericsson.retrospective.repository.RetrospectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RetrospectiveRestController {

    @Autowired
    private RetrospectiveRepository retrospectiveRepository;

    @Autowired
    private ItemRepository itemRepository;

    public RetrospectiveRestController(RetrospectiveRepository retrospectiveRepository){
        this.retrospectiveRepository = retrospectiveRepository;
    }

    @GetMapping("/retrospectives")
    public ResponseEntity<List<Retrospective>> getAll(){
        List<Retrospective> retrospectives = retrospectiveRepository.findAll();
        return ResponseEntity.ok().body(retrospectives);
    }

    @DeleteMapping("/delRetrospectives")
    public ResponseEntity<String> deleteAll(){
        retrospectiveRepository.deleteAll();
        return ResponseEntity.ok("All is deleted");
    }

    @PostMapping(value = "/addRetrospective")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Retrospective> addRetrospective(@RequestParam String name){
        Retrospective retrospective = new Retrospective(name);
        retrospectiveRepository.save(retrospective);
        Long id = retrospective.getRetrospectiveId();
        URI uri = URI.create("/addRetrospective/"+id);
        return ResponseEntity.created(uri).body(retrospective);
    }

    @GetMapping(value = "/retrospectives/{id}", produces={"application/json"})
    public ResponseEntity<Retrospective> getRetrospective(@PathVariable long id){
        Optional<Retrospective> retro = retrospectiveRepository.findById(id);
        if(retro.isPresent()){
            return ResponseEntity.ok().body(retro.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/retrospectiveItems/{id}", produces={"application/json"})
    public ResponseEntity<List<Item>> getRetrospectiveItems(@PathVariable long id){
        Optional<Retrospective> retro = retrospectiveRepository.findById(id);
        if(retro.isPresent()){
            List<Long> listOfId = retro.get().getItemIdList();
            List<Item> listOfItem = new ArrayList<>();
            for (Long i: listOfId) {
                Optional<Item> optionalItem =
                        itemRepository.findById(i);
                if (optionalItem.isPresent()) {
                    listOfItem.add(optionalItem.get());
                }
            }
            return ResponseEntity.ok().body(listOfItem);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/addSprintItem/{id}")
    public ResponseEntity<Item> addSprintItem(@PathVariable long id,
                                              @RequestParam Category mood,
                                              @RequestParam String itemDesc){
        Optional<Retrospective> r = retrospectiveRepository.findById(id);
        if(r.isPresent()){
            Item i = new Item(mood, itemDesc);
            itemRepository.save(i);

            Retrospective t = r.get();
            t.addItem(i.getItemId());
            retrospectiveRepository.save(t);
            return ResponseEntity.ok().body(i);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

