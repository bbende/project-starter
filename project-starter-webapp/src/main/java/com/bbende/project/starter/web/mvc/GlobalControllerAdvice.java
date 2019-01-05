package com.bbende.project.starter.web.mvc;

import com.bbende.project.starter.service.ProjectDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controller advice that populates model attributes needed by all controllers.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    private ProjectDetailsService projectDetailsService;

    @Autowired
    public GlobalControllerAdvice(final ProjectDetailsService projectDetailsService) {
        this.projectDetailsService = projectDetailsService;
    }

    @ModelAttribute
    public void addProjectDetails(final Model model) {
        model.addAttribute("projectDetails", projectDetailsService.getProjectDetails());
    }
}
