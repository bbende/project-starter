package com.bbende.project.starter.mapper;

import com.bbende.project.starter.model.Person;
import com.bbende.project.starter.dto.PersonDTO;

public class ModelMapper {

    public static PersonDTO map(final Person person) {
        final PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAge(person.getAge());
        dto.setCreated(person.getCreated().getTime());
        dto.setUpdated(person.getUpdated().getTime());
        return dto;
    }

    public static Person map(final PersonDTO personDTO) {
        final Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setAge(personDTO.getAge());
        return person;
    }
}
