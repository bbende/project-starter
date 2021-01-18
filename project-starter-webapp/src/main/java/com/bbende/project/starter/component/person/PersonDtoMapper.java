package com.bbende.project.starter.component.person;

class PersonDtoMapper {

    public static PersonDto map(final Person person) {
        final PersonDto dto = new PersonDto();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAge(person.getAge());
        dto.setCreated(person.getCreated().getTime());
        dto.setUpdated(person.getUpdated().getTime());
        return dto;
    }

    public static Person map(final PersonDto personDTO) {
        final Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setAge(personDTO.getAge());
        return person;
    }
}
