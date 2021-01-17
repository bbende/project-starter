package com.bbende.project.starter.web.api.resource;

import com.bbende.project.starter.core.commons.dto.ListDto;
import com.bbende.project.starter.core.modules.person.PersonDto;
import com.bbende.project.starter.web.api.RestIT;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonResourceIT extends RestIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResourceIT.class);

    private WebTarget peopleTarget;

    @BeforeEach
    public void setupPersonResourceIT() {
        final String peopleResourceUrl = createResourceUrl("/people");
        peopleTarget = client.target(peopleResourceUrl);
    }

    @Test
    public void testPersonResource() {
        // Verify we start with 2 people
        final ListDto<PersonDto> initialPeople = peopleTarget.request()
                .get(new GenericType<ListDto<PersonDto>>() {});
        assertNotNull(initialPeople);
        assertNotNull(initialPeople.getElements());
        assertEquals(0, initialPeople.getElements().size());

        // Create a person
        final PersonDto person = new PersonDto();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(21);

        final PersonDto createdPerson = peopleTarget.request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE),
                        PersonDto.class);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertEquals(person.getFirstName(), createdPerson.getFirstName());
        assertEquals(person.getLastName(), createdPerson.getLastName());
        assertEquals(person.getAge(), createdPerson.getAge());

        // Verify we have 3 people now
        final ListDto<PersonDto> peopleAfterCreate = peopleTarget.request()
                .get(new GenericType<ListDto<PersonDto>>() {});
        assertEquals(1, peopleAfterCreate.getElements().size());

        // Verify we can retrieve by id
        final PersonDto retrievedPerson = peopleTarget
                .path("/{id}")
                .resolveTemplate("id", createdPerson.getId())
                .request()
                .get(PersonDto.class);

        assertNotNull(retrievedPerson);
        assertEquals(createdPerson.getId(), retrievedPerson.getId());
        assertEquals(createdPerson.getFirstName(), retrievedPerson.getFirstName());
        assertEquals(createdPerson.getLastName(), retrievedPerson.getLastName());
        assertEquals(createdPerson.getAge(), retrievedPerson.getAge());

        // Verify we can update the person
        final PersonDto partialUpdate = new PersonDto();
        partialUpdate.setFirstName("UPDATED");

        final PersonDto updatedPerson = peopleTarget
                .path("/{id}")
                .resolveTemplate("id", createdPerson.getId())
                .request()
                .put(Entity.entity(partialUpdate, MediaType.APPLICATION_JSON_TYPE), PersonDto.class);

        assertNotNull(updatedPerson);
        assertEquals(retrievedPerson.getId(), updatedPerson.getId());
        assertEquals(partialUpdate.getFirstName(), updatedPerson.getFirstName());
        assertEquals(retrievedPerson.getLastName(), updatedPerson.getLastName());
        assertEquals(retrievedPerson.getAge(), updatedPerson.getAge());

        // Verify we can delete
        peopleTarget.path("/{id}")
                .resolveTemplate("id", createdPerson.getId())
                .request()
                .delete();

        // Verify we have 2 people again
        final ListDto<PersonDto> peopleAfterDelete = peopleTarget.request()
                .get(new GenericType<ListDto<PersonDto>>() {});
        assertEquals(0, peopleAfterDelete.getElements().size());

        // Verify we can't create an invalid person
        final PersonDto invalidPerson = new PersonDto();

        try {
            peopleTarget.request()
                    .post(Entity.entity(invalidPerson, MediaType.APPLICATION_JSON_TYPE),
                            PersonDto.class);
            Assert.fail("Should have thrown exception");
        } catch (BadRequestException e) {
            //LOGGER.debug(e.getMessage(), e);
        }
    }
}
