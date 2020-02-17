package com.explore.zookeeper.service.interfaces;

import com.explore.zookeeper.model.Person;

import java.util.List;

public interface ISyncService {

    void syncPersons(String masterHostAndPort);

    void replicatePerson(Person person, List<String> childNodes);
}
