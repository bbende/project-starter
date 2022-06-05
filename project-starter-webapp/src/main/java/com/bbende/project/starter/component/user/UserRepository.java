package com.bbende.project.starter.component.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findOneByUsername(String username);

}
