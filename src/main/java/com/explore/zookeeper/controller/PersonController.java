package com.explore.zookeeper.controller;

import com.explore.zookeeper.model.ClusterInfo;
import com.explore.zookeeper.model.Person;
import com.explore.zookeeper.service.interfaces.IPersonService;
import com.explore.zookeeper.service.interfaces.ISyncService;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final IPersonService personService;
    private final ISyncService syncService;

    public PersonController(IPersonService personService, ISyncService syncService) {
        this.personService = personService;
        this.syncService = syncService;
    }

    @PutMapping
    public Response save(@RequestBody Person person){
        personService.save(person);

        if(ClusterInfo.getInstance().isMaster()){
            syncService.replicatePerson(person, ClusterInfo.getInstance().getChildNodes());
        }

        return Response.ok()
                .entity("Saved Person successfully").build();
    }

    @GetMapping("/{id}")
    public Person get(@PathVariable("id") Long id){
        return personService.get(id);
    }
}