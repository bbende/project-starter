package com.bbende.project.starter.common.exception;

/**
 * An exception that is thrown when an entity is not found.
 */
public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

}
