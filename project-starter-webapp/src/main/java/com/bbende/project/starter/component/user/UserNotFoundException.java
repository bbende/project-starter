package com.bbende.project.starter.component.user;

import com.bbende.project.starter.component.common.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
