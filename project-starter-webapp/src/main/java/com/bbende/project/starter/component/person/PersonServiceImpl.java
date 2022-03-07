package com.bbende.project.starter.component.person;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonDtoMapper dtoMapper;

    @Autowired
    public PersonServiceImpl(final PersonRepository personRepository,
                             final PersonDtoMapper dtoMapper) {
        this.personRepository = personRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public List<PersonDto> getAll() {
        final List<PersonDto> people = new ArrayList<>();
        personRepository.findAll().forEach(p -> people.add(dtoMapper.toDto(p)));
        return people;
    }

    @Override
    public PersonDto get(final String id) {
        Validate.notBlank(id, "Person id is required");
        final Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return dtoMapper.toDto(person);
    }

    @Override
    public PersonDto create(final PersonDto personDTO) {
        Validate.notNull(personDTO, "PersonDTO cannot be null");

        final Person person = dtoMapper.toEntity(personDTO);
        person.setId(UUID.randomUUID().toString());

        final Person createdPerson = personRepository.save(person);
        return dtoMapper.toDto(createdPerson);
    }

    @Override
    public PersonDto update(final PersonDto personDTO) {
        Validate.notNull(personDTO, "PersonDTO cannot be null");

        final String personId = personDTO.getId();
        Validate.notBlank(personId, "PersonDTO id is required");

        final Person existingPerson = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(personId));

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
        return dtoMapper.toDto(updatedPerson);
    }

    @Override
    public PersonDto delete(final String id) {
        Validate.notBlank(id, "Person id is required");
        final Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        personRepository.deleteById(person.getId());
        return dtoMapper.toDto(person);
    }

}
