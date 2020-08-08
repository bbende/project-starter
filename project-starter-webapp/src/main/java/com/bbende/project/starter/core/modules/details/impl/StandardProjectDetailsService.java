package com.bbende.project.starter.core.modules.details.impl;

import com.bbende.project.starter.core.config.ProjectStarterProperties;
import com.bbende.project.starter.core.modules.details.ProjectDetailsDto;
import com.bbende.project.starter.core.modules.details.ProjectDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class StandardProjectDetailsService implements ProjectDetailsService {

    private final ProjectStarterProperties properties;

    @Autowired
    public StandardProjectDetailsService(final ProjectStarterProperties properties) {
        this.properties = properties;
    }

    @Override
    public ProjectDetailsDto getProjectDetails() {
        final ProjectDetailsDto projectDetails = new ProjectDetailsDto();
        projectDetails.setVersion("v" + properties.getProject().getVersion());
        projectDetails.setLabel(properties.getProject().getLabel());
        return projectDetails;
    }
}
