package com.bbende.project.starter.component.about;

import com.bbende.project.starter.ProjectStarterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class AboutServiceImpl implements AboutService {

    private final ProjectStarterProperties properties;

    @Autowired
    public AboutServiceImpl(final ProjectStarterProperties properties) {
        this.properties = properties;
    }

    @Override
    public AboutInfoDto getAboutInfo() {
        final AboutInfoDto projectDetails = new AboutInfoDto();
        projectDetails.setVersion("v" + properties.getProject().getVersion());
        projectDetails.setLabel(properties.getProject().getLabel());
        return projectDetails;
    }
}
