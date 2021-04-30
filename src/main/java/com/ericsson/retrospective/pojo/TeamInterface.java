package com.ericsson.retrospective.pojo;

import java.util.List;

public interface TeamInterface {
    String getTeamName();
    void setTeamName(String teamName);
    List<Member> getMembers();
    void addMember(Member m);
    void setMembers(List<Member> members);
    long getTeamId();
    List<Long> getRetrospectiveIds();
    void setRetrospectiveIds(List<Long> retrospectiveIds);
    Team addRetrospectiveId(long retrospectiveId);
}
