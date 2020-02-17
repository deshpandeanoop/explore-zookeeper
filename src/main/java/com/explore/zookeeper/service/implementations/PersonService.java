package com.explore.zookeeper.service.implementations;

import com.explore.zookeeper.model.Person;
import com.explore.zookeeper.repository.PersonRepository;
import com.explore.zookeeper.service.interfaces.IPersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonService implements IPersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

    @Override
    public Person get(Long id) {
        return personRepository.getPersonById(id);
    }
}
