package com.explore.zookeeper.service.implementations;

import com.explore.zookeeper.model.ClusterInfo;
import com.explore.zookeeper.model.Person;
import com.explore.zookeeper.repository.PersonRepository;
import com.explore.zookeeper.service.interfaces.ISyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SyncService implements ISyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncService.class);
    private final PersonRepository personRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public SyncService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void syncPersons(String masterHostAndPort) {
        LOGGER.info("Sync person data with master {}", masterHostAndPort);
        if(masterHostAndPort.equals(ClusterInfo.getServerHostAndPort())){
            return;
        }

        Long lastPersonId = personRepository.getLastPersonId();
        lastPersonId = null != lastPersonId ? lastPersonId : new Long(0);

        LOGGER.info("Last person Id {}", lastPersonId);
        ++ lastPersonId; // Syn should happen for persons having Id greater than last person id

        while (true){
            Person person = restTemplate.getForObject("http://"+masterHostAndPort+"/persons/"+lastPersonId, Person.class);
            if(null == person){
                break;
            }
            personRepository.save(person);
            LOGGER.info("Sync person Id {} successful", lastPersonId);
            ++ lastPersonId;
        }
    }

    @Override
    public void replicatePerson(Person person, List<String> childNodes) {
        LOGGER.info("Replicating person data to child nodes, {}", childNodes);

        childNodes.forEach(childNode -> restTemplate.put("http://"+childNode+"/persons/", person));
    }
}