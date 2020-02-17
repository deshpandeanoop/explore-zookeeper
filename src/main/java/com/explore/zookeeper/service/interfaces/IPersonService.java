package com.explore.zookeeper.service.interfaces;

import com.explore.zookeeper.model.Person;

public interface IPersonService {

    void save(Person person);

    Person get(Long id);
}