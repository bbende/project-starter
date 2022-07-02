package com.bbende.project.starter.security;

import com.bbende.project.starter.component.user.AuthorityDto;
import com.bbende.project.starter.component.user.AuthorityName;
import com.bbende.project.starter.component.user.UserDto;
import com.bbende.project.starter.component.user.UserNotFoundException;
import com.bbende.project.starter.component.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of Spring Security's {@link UserDetailsService} which delegates to
 * the provided {@link UserService} and converts a {@link UserDto} to a Spring
 * {@link org.springframework.security.core.userdetails.User}.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserDto userDto;
        try {
            userDto = userService.findByUsername(username);
        } catch (final UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }

        final Collection<? extends GrantedAuthority> authorities =
                userDto.getAuthorities().stream()
                        .map(AuthorityDto::getName)
                        .map(AuthorityName::toString)
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                userDto.getUsername(), userDto.getPasswordHash(), authorities);
    }

}
