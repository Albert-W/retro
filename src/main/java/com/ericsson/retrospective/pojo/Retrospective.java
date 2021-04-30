package com.ericsson.retrospective.pojo;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Retrospective implements RetrospectiveInterface{

    @Id
    private long retrospectiveId;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private String name;
    private List<Long> itemIdList = new ArrayList<>();

    public static void setAtomicInteger(AtomicInteger id){
        atomicInteger = id;
    }

    public Retrospective(long retrospectiveId, String name, List<Long> itemIdList) {
        this.retrospectiveId = retrospectiveId;
        this.name = name;
        this.itemIdList = itemIdList;
    }

    public Retrospective() {
    }

    public Retrospective(String name) {
        this.retrospectiveId = atomicInteger.incrementAndGet();
        this.name = name;
    }

    @Override
    public long getRetrospectiveId() {
        return this.retrospectiveId;
    }

    @Override
    public void setRetrospectiveId(long retrospectiveId) {
        this.retrospectiveId = retrospectiveId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Long> getItemIdList() {
        return this.itemIdList;
    }

    @Override
    public void setItemIdList(List<Long> itemIdList) {
        this.itemIdList = itemIdList;
    }

    @Override
    public void addItem(long itemId){
        this.itemIdList.add(itemId);
    }
}
