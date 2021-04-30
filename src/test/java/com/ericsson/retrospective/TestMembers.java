package com.ericsson.retrospective;

import com.ericsson.retrospective.pojo.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestMembers {


    @BeforeEach
    void init() {
        Member.setAtomicInteger(new AtomicInteger(0));
    }


    @Test
    void testSetGetName() {
        Member member = new Member("John");
        member.setName("Bill");
        assertEquals("Bill", member.getName());
    }

    @Test
    void testToString() {
        Member member = new Member("Bill");
        assertTrue(member.toString().contains("Bill"));
    }

    @Test
    void testGetMemberId() {
        Member member = new Member("Bill");
        member.setMemberId(5);
        assertEquals(5, member.getMemberId());
    }

}
