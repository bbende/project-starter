package com.bbende.project.starter.component.details;

import com.bbende.project.starter.ProjectStarterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ProjectDetailsServiceImpl implements ProjectDetailsService {

    private final ProjectStarterProperties properties;

    @Autowired
    public ProjectDetailsServiceImpl(final ProjectStarterProperties properties) {
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
