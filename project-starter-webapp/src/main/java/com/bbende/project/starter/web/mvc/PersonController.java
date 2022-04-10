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
import java.util.List;

import static com.bbende.project.starter.web.mvc.Unpoly.UNPOLY;

@Controller
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/people")
    public ModelAndView getPeople(@ModelAttribute(UNPOLY) final Unpoly unpoly) {
        final List<PersonDto> people = personService.getAll();
        final ModelMap modelMap = new ModelMap("people", people);
        return new ModelAndView(unpoly.getView("people/list"), modelMap);
    }

    @GetMapping("/people/new")
    public ModelAndView newPerson(@ModelAttribute(UNPOLY) final Unpoly unpoly) {
        final ModelMap modelMap = new ModelMap("person", new PersonDto());
        return new ModelAndView(unpoly.getView("people/new"), modelMap);
    }

    @PostMapping("/people")
    public ModelAndView createPerson(
            @ModelAttribute(UNPOLY) final Unpoly unpoly,
            @Valid @ModelAttribute("person") final PersonDto person,
            final BindingResult personBinding) {
        if (personBinding.hasErrors()) {
            return new ModelAndView(unpoly.getView("people/new"));
        } else {
            personService.create(person);
            if (unpoly.isXUpTarget()) {
                return getPeople(unpoly);
            } else {
                return new ModelAndView("redirect:/people");
            }
        }
    }

    @GetMapping("/people/{id}/delete/confirm")
    public ModelAndView deleteConfirm(
            @PathVariable final String id,
            @ModelAttribute(UNPOLY) final Unpoly unpoly) {
        final PersonDto person = personService.get(id);
        final ModelMap modelMap = new ModelMap("person", person);
        if (unpoly.isXUpTarget()) {
            return new ModelAndView(unpoly.getView("people/delete-confirm"), modelMap);
        } else {
            return new ModelAndView("people/delete-confirm", modelMap);
        }
    }

    @DeleteMapping("/people/{id}")
    public ModelAndView delete(
            @PathVariable final String id,
            @ModelAttribute(UNPOLY) final Unpoly unpoly) {
        personService.delete(id);
        if (unpoly.isXUpTarget()) {
            return getPeople(unpoly);
        } else {
            return new ModelAndView("redirect:/people");
        }
    }

}
