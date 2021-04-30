package com.ericsson.retrospective.pojo;

import java.util.List;

public interface RetrospectiveInterface {
    long getRetrospectiveId();
    void setRetrospectiveId(long retrospectiveId);
    String getName();
    void setName(String name);
    List<Long> getItemIdList();
    void setItemIdList(List<Long> itemIdList);
    void addItem(long itemId);
}
