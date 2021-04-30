package com.ericsson.retrospective.pojo;

import java.util.List;

public interface ItemInterface {
    int getVotes();
    void setVotes(int votes);
    Category getCategory();
    void setCategory(Category category);
    String getDescription();
    void setDescription(String description);
    void addComments(String comment);
    List<String> getComments();
    long getItemId();
    void setItemId(long itemId);

}
