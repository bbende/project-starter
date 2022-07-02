package com.bbende.project.starter.component.person.impl;

import com.bbende.project.starter.component.common.dto.DtoMapper;
import com.bbende.project.starter.component.person.PersonDto;
import org.springframework.stereotype.Service;

@Service
class PersonDtoMapper implements DtoMapper<PersonDto, Person> {

    @Override
    public PersonDto toDto(final Person entity) {
        final PersonDto dto = new PersonDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(entity.getAge());
        dto.setCreated(entity.getCreated().getTime());
        dto.setUpdated(entity.getUpdated().getTime());
        return dto;
    }

}
