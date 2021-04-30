package com.ericsson.retrospective;

import com.ericsson.retrospective.pojo.Member;
import com.ericsson.retrospective.pojo.Team;
import com.ericsson.retrospective.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestTeam {
    @Autowired
    TeamRepository teamRepository;

    @BeforeEach
    void init() {
        teamRepository.deleteAll();
        Team.setAtomicInteger(new AtomicInteger(0));
    }

    @Test
    void testBasicName() {
        Team team = new Team("Red",
                Arrays.asList(new Member("James"), new Member("Bob"), new Member("Ross")));
        assertEquals("Red", team.getTeamName());
    }

    @Test
    void testSetTeamName() {
        Team team = new Team("Red",
                Arrays.asList(new Member("James"), new Member("Jin"), new Member("John")));
        team.setTeamName("Blue");
        assertEquals("Blue", team.getTeamName());
    }

    @Test
    void testGetMembers() {
        Team team = new Team("Blue",
                Arrays.asList(new Member("James"), new Member("Jin"), new Member("John")));
        List<Member> members = Arrays.asList(new Member("James"), new Member("Jin"), new Member("John"));
        team.setMembers(members);
        assertEquals(members, team.getMembers());
    }

    @Test
    void testSetMembers() {
        Team team = new Team("Green",
                Arrays.asList(new Member("James"), new Member("Jin"), new Member("John")));
        List<Member> members = Arrays.asList(new Member("James"),
                new Member("Jin"), new Member("John"), new Member("Oliver"));
        team.setMembers(members);
        assertEquals(4, team.getMembers().size());
    }

    @Test
    void testToString() {
        Team team = new Team("Purple",
                Arrays.asList(new Member("James"), new Member("Jin"), new Member("John")));
        assertTrue(team.toString().contains("Jin"));
    }


}
