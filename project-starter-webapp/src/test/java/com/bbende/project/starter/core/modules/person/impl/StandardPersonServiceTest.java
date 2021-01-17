package com.bbende.project.starter.core.modules.person.impl;

import com.bbende.project.starter.core.modules.person.PersonDto;
import com.bbende.project.starter.core.modules.person.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StandardPersonServiceTest {

    private PersonRepository personRepository;
    private PersonService personService;

    @BeforeEach
    public void setup() {
        personRepository = Mockito.mock(PersonRepository.class);
        personService = new StandardPersonService(personRepository);
    }

    @Test
    public void testFindAll() {
        final Person p1 = new Person();
        p1.setId(UUID.randomUUID().toString());
        p1.setFirstName("John");
        p1.setLastName("Smith");
        p1.setAge(21);
        p1.setCreated(new Date());
        p1.setUpdated(new Date());

        final Person p2 = new Person();
        p2.setId(UUID.randomUUID().toString());
        p2.setFirstName("Bob");
        p2.setLastName("Smith");
        p2.setAge(31);
        p2.setCreated(new Date());
        p2.setUpdated(new Date());

        when(personRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        final List<PersonDto> people = personService.getAll();
        assertEquals(2, people.size());
    }
}
