package com.bbende.project.starter.component.person.impl;

import com.bbende.project.starter.test.DatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Insert test data into DB tables, see super class for how DB is reset
@Sql(scripts = {"classpath:db/insertPersonData.sql"})
public class PersonRepositoryIT extends DatabaseIT {

    @Autowired
    private PersonRepository repository;

    @Test
    public void testCreate() {
        final Person person = new Person();
        person.setId(UUID.randomUUID().toString());
        person.setFirstName("Foo");
        person.setLastName("Bar");
        person.setAge(21);

        repository.save(person);

        final Optional<Person> retrievePerson = repository.findById(person.getId());
        assertTrue(retrievePerson.isPresent());
        assertEquals(person.getId(), retrievePerson.get().getId());
        assertEquals(person.getFirstName(), retrievePerson.get().getFirstName());
        assertEquals(person.getLastName(), retrievePerson.get().getLastName());
        assertEquals(person.getAge(), retrievePerson.get().getAge());

        assertNotNull(retrievePerson.get().getCreated());
        assertNotNull(retrievePerson.get().getUpdated());
    }

    @Test
    public void testFindById() {
        final Optional<Person> person = repository.findById("P1");
        assertTrue(person.isPresent());
        assertEquals("P1", person.get().getId());

        // Verify the one-to-many relationship
        assertNotNull(person.get().getEvents());
        assertEquals(2, person.get().getEvents().size());
    }

    @Test
    public void testFindAll() {
        final Iterable<Person> people = repository.findAll();

        final AtomicInteger count = new AtomicInteger(0);
        people.forEach(p -> count.incrementAndGet());
        assertEquals(2, count.get());
    }

    @Test
    public void testFindByFirstName() {
        final Iterable<Person> people = repository.findByFirstName("Alice");

        final AtomicInteger count = new AtomicInteger(0);
        people.forEach(p -> count.incrementAndGet());
        assertEquals(1, count.get());

        final Person person = people.iterator().next();
        assertEquals("P1", person.getId());
    }

    @Test
    public void testFindByLastName() {
        final Iterable<Person> people = repository.findByLastName("Jones");

        final AtomicInteger count = new AtomicInteger(0);
        people.forEach(p -> count.incrementAndGet());
        assertEquals(1, count.get());

        final Person person = people.iterator().next();
        assertEquals("P2", person.getId());
    }

    @Test
    public void testDelete() {
        final Optional<Person> retrievePerson = repository.findById("P1");
        assertTrue(retrievePerson.isPresent());

        repository.delete(retrievePerson.get());

        final Optional<Person> deletedPerson = repository.findById("P1");
        assertFalse(deletedPerson.isPresent());
    }
}
