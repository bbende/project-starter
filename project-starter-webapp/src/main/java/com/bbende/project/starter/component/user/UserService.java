package com.bbende.project.starter.component.user;

/**
 * Service for managing users.
 */
public interface UserService {

    UserDto findByUsername(String username);

}
