package com.bbende.project.starter.core.modules.person.impl;

import org.springframework.data.repository.PagingAndSortingRepository;

interface PersonRepository extends PagingAndSortingRepository<Person,String> {

    Iterable<Person> findByFirstName(String firstName);

    Iterable<Person> findByLastName(String lastName);

}
