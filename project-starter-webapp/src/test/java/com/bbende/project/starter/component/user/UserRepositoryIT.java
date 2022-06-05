package com.bbende.project.starter.component.user;

import com.bbende.project.starter.test.DatabaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryIT extends DatabaseIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testRetrieveInitialAdminUser() {
        Optional<User> adminUser = userRepository.findOneByUsername("admin");
        assertTrue(adminUser.isPresent());
        assertEquals("admin", adminUser.get().getUsername());

        Set<Authority> adminAuthorities = adminUser.get().getAuthorities();
        assertEquals(2, adminAuthorities.size());

        assertNotNull(adminAuthorities.stream()
                .filter(a -> a.getName() == AuthorityName.ROLE_ADMIN)
                .findFirst()
                .orElse(null));

        assertNotNull(adminAuthorities.stream()
                .filter(a -> a.getName() == AuthorityName.ROLE_USER)
                .findFirst()
                .orElse(null));

    }

    @Test
    public void testRetrieveInitialUserUser() {
        Optional<User> userUser = userRepository.findOneByUsername("user");
        assertTrue(userUser.isPresent());
        assertEquals("user", userUser.get().getUsername());

        Set<Authority> adminAuthorities = userUser.get().getAuthorities();
        assertEquals(1, adminAuthorities.size());

        assertNotNull(adminAuthorities.stream()
                .filter(a -> a.getName() == AuthorityName.ROLE_USER)
                .findFirst()
                .orElse(null));
    }

}
