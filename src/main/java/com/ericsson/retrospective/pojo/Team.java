package com.ericsson.retrospective.pojo;

import com.ericsson.retrospective.repository.RetrospectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Team implements TeamInterface{
    @Id
    private long teamId;
    private static AtomicInteger atomicInteger= new AtomicInteger(0);
    private String teamName;
    private List<Member> members = new ArrayList<>();
    private List<Long> retrospectiveIds = new ArrayList<>();

    @Autowired
    private RetrospectiveRepository retrospectiveRepository;

    public static void setAtomicInteger(AtomicInteger a){
        atomicInteger = a;
    }

    public Team(String teamName){

        this.teamId = atomicInteger.incrementAndGet();
        this.teamName = teamName;
        this.members = new ArrayList<>();
    }

    public Team(String teamName, List<Member> members) {
        this.teamId = atomicInteger.incrementAndGet();
        this.teamName = teamName;
        this.members = members;
    }

    public Team(long teamId, String teamName, List<Member> members) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.members = members;
    }

    public Team() {
    }

    @Override
    public String getTeamName() {
        return this.teamName;
    }
    @Override
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    @Override
    public List<Member> getMembers() {
        return members;
    }

    @Override
    public void addMember(Member m){
        this.members.add(m);
    }
    @Override
    public void setMembers(List<Member> members) {
        this.members = members;
    }
    @Override
    public long getTeamId() {
        return teamId;
    }
    @Override
    public List<Long> getRetrospectiveIds() {
        return retrospectiveIds;
    }
    @Override
    public void setRetrospectiveIds(List<Long> retrospectiveIds) {
        this.retrospectiveIds = retrospectiveIds;
    }
    @Override
    public Team addRetrospectiveId(long retrospectiveId) {
        this.retrospectiveIds.add(retrospectiveId);
        return this;
    }


    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", members=" + members +
                ", retrospectiveIds=" + retrospectiveIds +
                '}';
    }
}
