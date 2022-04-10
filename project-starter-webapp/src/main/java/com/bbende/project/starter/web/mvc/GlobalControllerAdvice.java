package com.bbende.project.starter.web.mvc;

import com.bbende.project.starter.common.exception.EntityNotFoundException;
import com.bbende.project.starter.component.details.ProjectDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller advice that will be applied to all controllers.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    private ProjectDetailsService projectDetailsService;

    @Autowired
    public GlobalControllerAdvice(final ProjectDetailsService projectDetailsService) {
        this.projectDetailsService = projectDetailsService;
    }

    // -- Model attributes to make available to all controllers

    @ModelAttribute
    public void addProjectDetails(final Model model) {
        model.addAttribute("projectDetails", projectDetailsService.getProjectDetails());
    }

    @ModelAttribute(Unpoly.UNPOLY)
    public Unpoly getUnpoly(@RequestHeader final HttpHeaders headers) {
        return Unpoly.fromHeaders(headers);
    }

    // -- Exception handlers for all controllers

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(final EntityNotFoundException ex, final HttpServletResponse response) {
        final int statusCode = HttpStatus.NOT_FOUND.value();
        response.setStatus(statusCode);
        return createErrorModelAndView(statusCode, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleEntityNotFoundException(final IllegalArgumentException ex, final HttpServletResponse response) {
        final int statusCode = HttpStatus.BAD_REQUEST.value();
        response.setStatus(statusCode);
        return createErrorModelAndView(statusCode, ex);
    }

    private ModelAndView createErrorModelAndView(final int statusCode, final Throwable t) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("", t);
        }
        return createErrorModelAndView(statusCode, t.getMessage());
    }

    private ModelAndView createErrorModelAndView(final int statusCode, final String message) {
        final Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("status", statusCode);
        modelMap.put("message", message);
        return new ModelAndView("error", modelMap);
    }
}
