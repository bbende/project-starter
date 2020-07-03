package com.bbende.project.starter.core.modules.person.impl;

import com.bbende.project.starter.core.commons.exception.ResourceNotFoundException;
import com.bbende.project.starter.core.modules.person.PersonDto;
import com.bbende.project.starter.core.modules.person.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
class StandardPersonService implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public StandardPersonService(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonDto> getAll() {
        final List<PersonDto> people = new ArrayList<>();
        personRepository.findAll().forEach(p -> people.add(PersonDtoMapper.map(p)));
        return people;
    }

    @Override
    public PersonDto get(final String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Person id is required");
        }

        final Person existingPerson = getPersonOrThrow(id);
        return PersonDtoMapper.map(existingPerson);
    }

    @Override
    public PersonDto create(final PersonDto personDTO) {
        if (personDTO == null) {
            throw new IllegalArgumentException("PersonDTO cannot be null");
        }

        final Person person = PersonDtoMapper.map(personDTO);
        person.setId(UUID.randomUUID().toString());

        final Person createdPerson = personRepository.save(person);
        return PersonDtoMapper.map(createdPerson);
    }

    @Override
    public PersonDto update(final PersonDto personDTO) {
        if (personDTO == null) {
            throw new IllegalArgumentException("PersonDTO cannot be null");
        }

        if (StringUtils.isBlank(personDTO.getId())) {
            throw new IllegalArgumentException("PersonDTO id is required");
        }

        final Person existingPerson = getPersonOrThrow(personDTO.getId());

        if (!StringUtils.isBlank(personDTO.getFirstName())) {
            existingPerson.setFirstName(personDTO.getFirstName());
        }

        if (!StringUtils.isBlank(personDTO.getLastName())) {
            existingPerson.setLastName(personDTO.getLastName());
        }

        if (personDTO.getAge() != null) {
            existingPerson.setAge(personDTO.getAge());
        }

        final Person updatedPerson = personRepository.save(existingPerson);
        return PersonDtoMapper.map(updatedPerson);
    }

    @Override
    public PersonDto delete(final String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Person id is required");
        }

        final Person person = getPersonOrThrow(id);
        personRepository.deleteById(person.getId());
        return PersonDtoMapper.map(person);
    }

    private Person getPersonOrThrow(final String id) {
        final Optional<Person> person = personRepository.findById(id);
        if (!person.isPresent()) {
            throw new ResourceNotFoundException("A person with the specified id does not exist");
        }
        return person.get();
    }
}
