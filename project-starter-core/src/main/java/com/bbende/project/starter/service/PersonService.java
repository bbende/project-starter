package com.bbende.project.starter.service;

import com.bbende.project.starter.mapper.ModelMapper;
import com.bbende.project.starter.model.Person;
import com.bbende.project.starter.repository.PersonRepository;
import com.bbende.project.starter.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public PersonDTO create(final PersonDTO personDTO) {
        final Person person = ModelMapper.map(personDTO);
        person.setId(UUID.randomUUID().toString());
        person.setCreated(new Date());

        final Person createdPerson = personRepository.save(person);
        return ModelMapper.map(createdPerson);
    }

    public void delete(final String personId) {
        personRepository.deleteById(personId);
    }

}
