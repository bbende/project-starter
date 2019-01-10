package com.bbende.project.starter.service;

import com.bbende.project.starter.dto.PersonDTO;
import com.bbende.project.starter.exception.ResourceNotFoundException;
import com.bbende.project.starter.mapper.ModelMapper;
import com.bbende.project.starter.model.Person;
import com.bbende.project.starter.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> getAll() {
        final List<PersonDTO> people = new ArrayList<>();
        personRepository.findAll().forEach(p -> people.add(ModelMapper.map(p)));
        return people;
    }

    public PersonDTO get(final String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Person id is required");
        }

        final Person existingPerson = getPersonOrThrow(id);
        return ModelMapper.map(existingPerson);
    }

    public PersonDTO create(final PersonDTO personDTO) {
        if (personDTO == null) {
            throw new IllegalArgumentException("PersonDTO cannot be null");
        }

        final Person person = ModelMapper.map(personDTO);
        person.setId(UUID.randomUUID().toString());

        final Person createdPerson = personRepository.save(person);
        return ModelMapper.map(createdPerson);
    }

    public PersonDTO update(final PersonDTO personDTO) {
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
        return ModelMapper.map(updatedPerson);
    }

    public void delete(final String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Person id is required");
        }

        final Person person = getPersonOrThrow(id);
        personRepository.deleteById(person.getId());
    }

    private Person getPersonOrThrow(final String id) {
        final Optional<Person> person = personRepository.findById(id);
        if (!person.isPresent()) {
            throw new ResourceNotFoundException("A person with the specified id does not exist");
        }
        return person.get();
    }
}
