package com.bbende.project.starter.component.person;

import com.bbende.project.starter.common.dto.DtoMapper;
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

    @Override
    public Person toEntity(final PersonDto dto) {
        final Person entity = new Person();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAge(dto.getAge());
        return entity;
    }

}
