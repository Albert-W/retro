package com.ericsson.retrospective.rest_controller;

import com.ericsson.retrospective.pojo.Member;
import com.ericsson.retrospective.pojo.Retrospective;
import com.ericsson.retrospective.pojo.Team;
import com.ericsson.retrospective.repository.RetrospectiveRepository;
import com.ericsson.retrospective.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TeamRestController {
    private final TeamRepository teamRepository;

    @Autowired
    private RetrospectiveRepository retrospectiveRepository;

    public TeamRestController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> all(){
        List<Team> teams = teamRepository.findAll();
        return ResponseEntity.ok().body(teams);
    }

    @DeleteMapping("/delTeams")
    public ResponseEntity<String> deleteTeams(){
        teamRepository.deleteAll();
        return ResponseEntity.ok("All is deleted");
    }


    @PostMapping(value = "/addteam", produces={"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Team> addTeam(@RequestParam String teamName){
        Team team = new Team(teamName);
        teamRepository.save(team);
        Long id = team.getTeamId();
        URI uri = URI.create("/teams/"+id);
        return ResponseEntity.created(uri).body(team);
    }


    @GetMapping(value = "/teams/{id}", produces={"application/json"})
    public ResponseEntity<Team> getTeam(@PathVariable long id){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            return ResponseEntity.ok().body(team.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/teams/{id}", produces={"application/json"})
    public ResponseEntity<Team> updateTeamName(@PathVariable long id, @RequestParam String name){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            Team t = team.get();
            t.setTeamName(name);
            return ResponseEntity.ok().body(teamRepository.save(t));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/addTeamMember/{id}")
    public ResponseEntity<Team> addTeamMember(@PathVariable long id, @RequestParam String memberName){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            Team t = team.get();
            t.addMember(new Member(memberName));
            return ResponseEntity.ok().body(teamRepository.save(t));
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping(value = "/getTeamMembers/{id}", produces={"application/json"})
    public ResponseEntity<List<Member>> getMembers(@PathVariable long id){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            return ResponseEntity.ok().body(team.get().getMembers());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/getTeamSprints/{id}", produces={"application/json"})
    public ResponseEntity<List<Retrospective>> getTeamSprints(@PathVariable long id){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            List<Long> listOfId = team.get().getRetrospectiveIds();
            List<Retrospective> listOfRetro = new ArrayList<>();
            for (Long i: listOfId) {
                Optional<Retrospective> optionalRetrospective =
                        retrospectiveRepository.findById(i);
                if (optionalRetrospective.isPresent()) {
                    listOfRetro.add(optionalRetrospective.get());
                }
            }
            return ResponseEntity.ok().body(listOfRetro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/addTeamSprint/{id}")
    public ResponseEntity<Team> addTeamSprint(@PathVariable long id, @RequestParam String sprintName){
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){

            Team t = team.get();
            Retrospective r = new Retrospective(sprintName);
            retrospectiveRepository.save(r);
            t.addRetrospectiveId(r.getRetrospectiveId());
            teamRepository.save(t);
            return ResponseEntity.ok().body(t);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
