package com.ericsson.retrospective;

import com.ericsson.retrospective.pojo.Member;
import com.ericsson.retrospective.pojo.Retrospective;
import com.ericsson.retrospective.pojo.Team;
import com.ericsson.retrospective.repository.RetrospectiveRepository;
import com.ericsson.retrospective.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestTeamRest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RetrospectiveRepository retrospectiveRepository;


    @BeforeEach
    public void init(){
        teamRepository.deleteAll();
        Team.setAtomicInteger(new AtomicInteger(0));
    }
    @Test
    void getAllTeams() {
        Team team = new Team("Bob");
        teamRepository.save(team);
        ResponseEntity<List<Team>> entity = restTemplate.exchange(
                "/api/teams",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Team>>() {
                }
        );
        List<Team> teams = entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(1, teams.size());
    }

    @Test
    void getAllTeams_noneTeams(){
        ResponseEntity<List<Team>> entity = restTemplate.exchange(
                "/api/teams",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Team>>() {}
        );
        List<Team> teams= entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(0, teams.size());
    }

    @Test
    void deleteTeams(){
        Team team = new Team("Bob");
        teamRepository.save(team);

        restTemplate.delete("/api/delTeams");
        assertEquals(0, teamRepository.findAll().size());
    }

    @Test
    void addTeam(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("teamName","Team1");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Team> responseEntity = restTemplate.postForEntity(
                "/api/addteam",
                request,
                Team.class
        );
        Team responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Team1", responseBody.getTeamName());
        
    }

    @Test
    void getTeambyId(){
        Team team = new Team("Bob");
        teamRepository.save(team);
        ResponseEntity<Team> responseEntity = restTemplate.exchange(
                "/api/teams/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Team>() {}
        );
        Team responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Bob", responseBody.getTeamName());

    }

    @Test
    void updateTeamName(){
        Team team = new Team("Bob");
        teamRepository.save(team);

        restTemplate.put(
                "/api/teams/1?name=Jack",
                null
        );
        assertEquals("Jack", teamRepository.findById((long)1).get().getTeamName());
    }

    @Test
    void addTeamMember(){
        Team team = new Team("Bob");
        teamRepository.save(team);
        restTemplate.put(
                "/api/addTeamMember/1?memberName=Cathe",
                null
        );
        assertEquals("Cathe", teamRepository.findById((long)1).get().getMembers().get(0).getName());
    }

    @Test
    void addTeamMember_notFound() {
        Team team = new Team("Bob");
        teamRepository.save(team);
        ResponseEntity<Team> responseEntity = restTemplate.exchange(
                "/api/addTeamMember/10000?memberName=Cathe",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Team>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void updateTeamName_NotFound() {
        Team team = new Team("Bob");
        teamRepository.save(team);

        ResponseEntity<Team> responseEntity = restTemplate.exchange(
                "/api/teams/10000?name=Jack",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Team>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getTeamMembersById() {
        Team team = new Team("Bob");
        Member m = new Member("Kavin");
        team.addMember(m);
        teamRepository.save(team);

        ResponseEntity<List<Member>> responseEntity = restTemplate.exchange(
                "/api/getTeamMembers/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Member>>() {}
        );
        List<Member> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Kavin", responseBody.get(0).getName());
    }

    @Test
    void getTeamSprintById() {
        Team team = new Team("Bob");
        Retrospective r = new Retrospective("Sprint1");
        team.addRetrospectiveId(r.getRetrospectiveId());
        retrospectiveRepository.save(r);
        teamRepository.save(team);


        ResponseEntity<List<Retrospective>> responseEntity = restTemplate.exchange(
                "/api/getTeamSprints/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Retrospective>>() {}
        );
        List<Retrospective> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Sprint1", responseBody.get(0).getName());
    }


    @Test
    void addTeamSprint(){
        Team team = new Team("Bob");
        teamRepository.save(team);
        restTemplate.put(
                "/api/addTeamSprint/1?sprintName=Sprint1",
                null
        );
        long id = teamRepository.findById((long)1).get().getRetrospectiveIds().get(0);
        assertEquals("Sprint1", retrospectiveRepository.findById(id).get().getName());
    }

    @Test
    void getTeambyId_notFound(){
        Team team = new Team("Bob");
        teamRepository.save(team);
        ResponseEntity<Team> responseEntity = restTemplate.exchange(
                "/api/teams/100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Team>() {}
        );
        Team responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getTeamMembersById_notFound() {
        Team team = new Team("Bob");
        Member m = new Member("Kavin");
        team.addMember(m);
        teamRepository.save(team);

        ResponseEntity<List<Member>> responseEntity = restTemplate.exchange(
                "/api/getTeamMembers/1000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Member>>() {}
        );
        List<Member> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    void getTeamSprintById_notFound() {

        ResponseEntity<List<Retrospective>> responseEntity = restTemplate.exchange(
                "/api/getTeamSprints/1000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Retrospective>>() {}
        );
        List<Retrospective> responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    void addTeamSprint_notFound(){

         ResponseEntity<Team> responseEntity =  restTemplate.exchange(
                "/api/addTeamSprint/10000?sprintName=Jack",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Team>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
