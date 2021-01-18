package com.bbende.project.starter.component.details;

import com.bbende.project.starter.config.ProjectStarterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectDetailsServiceTest {

    private ProjectStarterProperties properties;
    private ProjectStarterProperties.Project project;

    private ProjectDetailsService projectDetailsService;

    @BeforeEach
    public void setup() {
        properties = mock(ProjectStarterProperties.class);
        project = mock(ProjectStarterProperties.Project.class);
        when(properties.getProject()).thenReturn(project);
        projectDetailsService = new ProjectDetailsServiceImpl(properties);
    }

    @Test
    public void testGetProjectDetails() {
        when(project.getVersion()).thenReturn("test-version");
        when(project.getLabel()).thenReturn("test-label");

        final ProjectDetailsDto details = projectDetailsService.getProjectDetails();
        assertEquals("vtest-version", details.getVersion());
        assertEquals("test-label", details.getLabel());
    }
}
