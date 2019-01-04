package com.bbende.project.starter.service;

import com.bbende.project.starter.config.ProjectStarterProperties;
import com.bbende.project.starter.dto.ProjectDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectDetailsService {

    private final ProjectStarterProperties properties;

    @Autowired
    public ProjectDetailsService(final ProjectStarterProperties properties) {
        this.properties = properties;
    }

    public ProjectDetailsDTO getProjectDetails() {
        final ProjectDetailsDTO projectDetails = new ProjectDetailsDTO();
        projectDetails.setVersion("v" + properties.getProject().getVersion());
        return projectDetails;
    }
}
