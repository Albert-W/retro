package com.ericsson.retrospective.pojo;

import org.springframework.data.annotation.Id;

import java.util.concurrent.atomic.AtomicInteger;

public class Member implements MemberInterface{
    @Id
    private long memberId;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private String name;

    public Member(String name) {
        this.name = name;
        this.memberId = atomicInteger.incrementAndGet();
    }

    public Member() {
    }

    public Member(long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public static void setAtomicInteger(AtomicInteger id){
        atomicInteger = id;
    }

    @Override
    public long getMemberId() {
        return this.memberId;
    }
    @Override
    public void setMemberId(long memberId) {
        this.memberId = memberId;
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
    public String toString() {
        return String.format("Team Member [Id=%s, Name=%s]", memberId, name);
    }
}
