package com.bbende.project.starter.component.user.impl;

import com.bbende.project.starter.component.common.dto.DtoMapper;
import com.bbende.project.starter.component.user.AuthorityDto;
import com.bbende.project.starter.component.user.UserDto;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
class UserMapper implements DtoMapper<UserDto, User> {

    @Override
    public UserDto toDto(final User entity) {
        final UserDto userDto = new UserDto();
        userDto.setIdentifier(entity.getId());
        userDto.setUsername(entity.getUsername());
        userDto.setPasswordHash(entity.getPasswordHash());
        userDto.setEmail(entity.getEmail());
        userDto.setFirstName(entity.getFirstName());
        userDto.setLastName(entity.getLastName());
        userDto.setActivated(entity.isActivated());
        userDto.setAuthorities(
                entity.getAuthorities().stream()
                        .map(a -> new AuthorityDto(a.getName()))
                        .collect(Collectors.toSet())
        );
        return userDto;
    }

}
