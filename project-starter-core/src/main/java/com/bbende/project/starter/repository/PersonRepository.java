package com.bbende.project.starter.repository;

import com.bbende.project.starter.model.Person;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonRepository extends PagingAndSortingRepository<Person,String> {

    Iterable<Person> findByFirstName(String firstName);

    Iterable<Person> findByLastName(String lastName);

}