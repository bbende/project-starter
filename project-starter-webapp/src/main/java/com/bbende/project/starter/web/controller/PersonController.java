package com.bbende.project.starter.web.controller;

import com.bbende.project.starter.dto.PersonDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PersonController {

    private Map<String,PersonDTO> people = new HashMap<>();

    @GetMapping("/people")
    public ModelAndView listPeople() {
        final Collection<PersonDTO> peopleCollection = people.values();
        final ModelMap modelMap = new ModelMap("people", peopleCollection);
        return new ModelAndView("people/list", modelMap);
    }

    @GetMapping("/people/new")
    public ModelAndView newPerson() {
        final ModelMap modelMap = new ModelMap("person", new PersonDTO());
        return new ModelAndView("people/new", modelMap);
    }

    @PostMapping("/people")
    public ModelAndView createPerson(
            @Valid
            @ModelAttribute(name = "person")
            final PersonDTO person,
            final BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("people/new");
        } else {
            person.setId(UUID.randomUUID().toString());
            people.put(person.getId(), person);
            return new ModelAndView("redirect:/people");
        }
    }

    @DeleteMapping("/people/{id}")
    public ModelAndView delete(@PathVariable final String id) {
        people.remove(id);
        return new ModelAndView("redirect:/people");
    }

}
