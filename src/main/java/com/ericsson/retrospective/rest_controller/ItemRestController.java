package com.ericsson.retrospective.rest_controller;

import com.ericsson.retrospective.pojo.Item;

import com.ericsson.retrospective.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ItemRestController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAll(){
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.ok().body(items);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getAItem(@PathVariable long id){
        Optional<Item> item = itemRepository.findById(id);
        if(item.isPresent()){
            return ResponseEntity.ok().body(item.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/item/{id}/comments")
    public ResponseEntity<List<String>> getComments(@PathVariable long id){
        Optional<Item> item = itemRepository.findById(id);
        if(item.isPresent()){
            List<String> listOfComments = item.get().getComments();
            return ResponseEntity.ok().body(listOfComments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{id}/vote")
    public ResponseEntity<Integer> getVote(@PathVariable long id){
        Optional<Item> item = itemRepository.findById(id);
        if(item.isPresent()){
            return ResponseEntity.ok().body(item.get().getVotes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/item/{id}/vote")
    public ResponseEntity<Item> putVote(@PathVariable long id){
        Optional<Item> item = itemRepository.findById(id);
        if(item.isPresent()){
            Item i = item.get();
            i.setVotes(i.getVotes()+1);
            itemRepository.save(i);
            return ResponseEntity.ok().body(i);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/item/{id}/comment")
    public ResponseEntity<Item> putComment(@PathVariable long id,
                                           @RequestParam String comment){
        Optional<Item> item = itemRepository.findById(id);
        if(item.isPresent()){
            Item i = item.get();
            i.addComments(comment);
            itemRepository.save(i);
            return ResponseEntity.ok().body(i);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
