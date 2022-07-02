package com.bbende.project.starter.component.person;

import com.bbende.project.starter.component.common.exception.EntityNotFoundException;

public class PersonNotFoundException extends EntityNotFoundException {

    public PersonNotFoundException(final String personId) {
        super("Person with id " + personId + " does not exist");
    }
}
