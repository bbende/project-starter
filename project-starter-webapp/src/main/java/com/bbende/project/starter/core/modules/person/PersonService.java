package com.bbende.project.starter.core.modules.person;

import java.util.List;

public interface PersonService {
    List<PersonDto> getAll();

    PersonDto get(String id);

    PersonDto create(PersonDto personDTO);

    PersonDto update(PersonDto personDTO);

    PersonDto delete(String id);
}
