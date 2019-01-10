package com.bbende.project.starter.web.api.resource;

import com.bbende.project.starter.dto.ListDTO;
import com.bbende.project.starter.dto.PersonDTO;
import com.bbende.project.starter.web.api.RestIT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class PersonResourceIT extends RestIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResourceIT.class);

    private WebTarget peopleTarget;

    @Before
    public void setupPersonResourceIT() {
        final String peopleResourceUrl = createResourceUrl("/people");
        peopleTarget = client.target(peopleResourceUrl);
    }

    @Test
    public void testPersonResource() {
        // Verify we start with 0 people
        final ListDTO<PersonDTO> initialPeople = peopleTarget.request()
                .get(new GenericType<ListDTO<PersonDTO>>() {});
        assertNotNull(initialPeople);
        assertNotNull(initialPeople.getElements());
        assertEquals(0, initialPeople.getElements().size());

        // Create a person
        final PersonDTO person = new PersonDTO();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(21);

        final PersonDTO createdPerson = peopleTarget.request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE),
                        PersonDTO.class);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertEquals(person.getFirstName(), createdPerson.getFirstName());
        assertEquals(person.getLastName(), createdPerson.getLastName());
        assertEquals(person.getAge(), createdPerson.getAge());

        // Verify we have 1 person now
        final ListDTO<PersonDTO> peopleAfterCreate = peopleTarget.request()
                .get(new GenericType<ListDTO<PersonDTO>>() {});
        assertEquals(1, peopleAfterCreate.getElements().size());

        // Verify we can retrieve by id
        final PersonDTO retrievedPerson = peopleTarget
                .path("/{id}")
                .resolveTemplate("id", createdPerson.getId())
                .request()
                .get(PersonDTO.class);

        assertNotNull(retrievedPerson);
        assertEquals(createdPerson.getId(), retrievedPerson.getId());
        assertEquals(createdPerson.getFirstName(), retrievedPerson.getFirstName());
        assertEquals(createdPerson.getLastName(), retrievedPerson.getLastName());
        assertEquals(createdPerson.getAge(), retrievedPerson.getAge());

        // Verify we can update the person
        final PersonDTO partialUpdate = new PersonDTO();
        partialUpdate.setFirstName("UPDATED");

        final PersonDTO updatedPerson = peopleTarget
                .path("/{id}")
                .resolveTemplate("id", createdPerson.getId())
                .request()
                .put(Entity.entity(partialUpdate, MediaType.APPLICATION_JSON_TYPE), PersonDTO.class);

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

        // Verify we have 0 people again
        final ListDTO<PersonDTO> peopleAfterDelete = peopleTarget.request()
                .get(new GenericType<ListDTO<PersonDTO>>() {});
        assertEquals(0, peopleAfterDelete.getElements().size());

        // Verify we can't create an invalid person
        final PersonDTO invalidPerson = new PersonDTO();

        try {
            peopleTarget.request()
                    .post(Entity.entity(invalidPerson, MediaType.APPLICATION_JSON_TYPE),
                            PersonDTO.class);
            Assert.fail("Should have thrown exception");
        } catch (BadRequestException e) {
            //LOGGER.debug(e.getMessage(), e);
        }
    }
}
