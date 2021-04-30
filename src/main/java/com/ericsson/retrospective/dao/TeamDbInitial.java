package com.ericsson.retrospective.dao;

import com.ericsson.retrospective.pojo.*;
import com.ericsson.retrospective.repository.ItemRepository;
import com.ericsson.retrospective.repository.RetrospectiveRepository;
import com.ericsson.retrospective.repository.TeamRepository;
import com.ericsson.retrospective.util.RandomIdentifier;
import com.ericsson.retrospective.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class TeamDbInitial {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RetrospectiveRepository retrospectiveRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostConstruct
    public void init(){

        teamRepository.save(new Team( "Red",
                Arrays.asList(new Member("James Red"), new Member("Bob Red"), new Member("Ross Red")))
                .addRetrospectiveId(getNewRetro("Red").getRetrospectiveId())
                .addRetrospectiveId(getNewRetro("Red").getRetrospectiveId())
        );

        teamRepository.save(new Team( "Blue",
                Arrays.asList(new Member("James Blue"), new Member("Bob Blue"), new Member("Ross Blue"))).addRetrospectiveId(getNewRetro("Blue").getRetrospectiveId())
        );

        teamRepository.save(new Team( "Green",
                Arrays.asList(new Member("James Green"), new Member("Bob Green"), new Member("Ross Green")))
                .addRetrospectiveId(getNewRetro("Green1").getRetrospectiveId())
                .addRetrospectiveId(getNewRetro("Green2").getRetrospectiveId())
                .addRetrospectiveId(getNewRetro("Green3").getRetrospectiveId())
        );

        teamRepository.save(new Team( "Brown",
                Arrays.asList(new Member("James Brown"), new Member("Bob Brown"), new Member("Ross Brown"))).addRetrospectiveId(getNewRetro("Brown Brown").getRetrospectiveId())
        );
        teamRepository.save(new Team( "Violet",
                Arrays.asList(new Member("James Violet"), new Member("Bob Violet"), new Member("Ross Violet")))
                .addRetrospectiveId(getNewRetro("Violet1").getRetrospectiveId())
                .addRetrospectiveId(getNewRetro("Violet2").getRetrospectiveId())
                .addRetrospectiveId(getNewRetro("Violet3").getRetrospectiveId())
                .addRetrospectiveId(getNewRetro("Violet4").getRetrospectiveId())
        );
    }

    private Retrospective getNewRetro(String s){
        Retrospective r = new Retrospective(s + RandomIdentifier.randomIdentifier());
        r.addItem(getNewItem(Category.GLAD, "Item content2 " + RandomIdentifier.randomIdentifier() + ":" + r.getName() ).getItemId());
        r.addItem(getNewItem(Category.MAD, "Item content2 " + RandomIdentifier.randomIdentifier() +   ":" + r.getName() ).getItemId());
        r.addItem(getNewItem(Category.SAD,  "Item content3 " + RandomIdentifier.randomIdentifier() + ":" + r.getName() ).getItemId());
        retrospectiveRepository.save(r);
        return r;
    }

    private Item getNewItem(Category c, String s){
        Item i = new Item(c, s);
        i.addComments("Comment content: " + RandomIdentifier.randomIdentifier() );
        i.addComments("Comment content: " + RandomIdentifier.randomIdentifier() );
        itemRepository.save(i);
        return  i;
    }
}
