package com.bbende.project.starter.web.mvc;

import com.bbende.project.starter.component.person.PersonDto;
import com.bbende.project.starter.component.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/people")
    public ModelAndView listPeople() {
        final ModelMap modelMap = new ModelMap("people", personService.getAll());
        return new ModelAndView("people/list", modelMap);
    }

    @GetMapping("/people/new")
    public ModelAndView newPerson() {
        final ModelMap modelMap = new ModelMap("person", new PersonDto());
        return new ModelAndView("people/new", modelMap);
    }

    @PostMapping("/people")
    public ModelAndView createPerson(
            @Valid
            @ModelAttribute(name = "person")
            final PersonDto person,
            final BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("people/new");
        } else {
            personService.create(person);
            return new ModelAndView("redirect:/people");
        }
    }

    @GetMapping("/people/{id}/delete/confirm")
    public ModelAndView deleteConfirm(@PathVariable final String id) {
        final PersonDto person = personService.get(id);
        final ModelMap modelMap = new ModelMap("person", person);
        return new ModelAndView("people/delete-confirm", modelMap);
    }

    @DeleteMapping("/people/{id}")
    public ModelAndView delete(@PathVariable final String id) {
        personService.delete(id);
        return new ModelAndView("redirect:/people");
    }

}
