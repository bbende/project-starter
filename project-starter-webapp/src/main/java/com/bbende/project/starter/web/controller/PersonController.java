package com.bbende.project.starter.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PersonController {

    @GetMapping("/people")
    public ModelAndView listPeople() {
        return new ModelAndView("people/list");
    }

    @GetMapping("/people/new")
    public ModelAndView newPerson() {
        return new ModelAndView("people/new");
    }

}
