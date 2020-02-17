package com.explore.zookeeper.repository;

import com.explore.zookeeper.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person getPersonById(Long id);

    @Query(value = "Select max(id) From person", nativeQuery = true)
    Long getLastPersonId();
}
