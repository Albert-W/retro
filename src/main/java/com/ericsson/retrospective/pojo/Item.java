package com.ericsson.retrospective.pojo;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Item implements ItemInterface{
    @Id
    private long itemId;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private int votes = 0;
    private Category value;
    private String description;
    private List<String> comments = new ArrayList<>();

    public static void setAtomicInteger(AtomicInteger a){
        atomicInteger = a;
    }

    public Item(String description) {
        this.itemId = atomicInteger.incrementAndGet();
        this.description = description;
    }

    public Item() {}

    public Item(Category value, String description){
        this.itemId = atomicInteger.incrementAndGet();
        this.value = value;
        this.description = description;
    }

    @Override
    public int getVotes() {
        return votes;
    }

    @Override
    public void setVotes(int votes) {
        this.votes = votes;
    }
    @Override
    public Category getCategory() { return value; }
    @Override
    public void setCategory(Category category) { this.value = category; }
    @Override
    public String getDescription() { return description; }
    @Override
    public void setDescription(String description) { this.description = description;}
    @Override
    public void addComments(String comment){
        comments.add(comment);
    }
    @Override
    public List<String> getComments(){ return comments; }
    @Override
    public String toString() {
        return String.format("Item [votes=%s, Category=%s, description=%s, comments=%s]", votes, value,
                description, comments);
    }
    @Override
    public long getItemId() {
        return this.itemId;
    }
    @Override
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
