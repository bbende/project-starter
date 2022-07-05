package com.bbende.project.starter.component.about;

import com.bbende.project.starter.ProjectStarterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AboutServiceTest {

    private ProjectStarterProperties properties;
    private ProjectStarterProperties.Project project;

    private AboutService aboutService;

    @BeforeEach
    public void setup() {
        properties = mock(ProjectStarterProperties.class);
        project = mock(ProjectStarterProperties.Project.class);
        when(properties.getProject()).thenReturn(project);
        aboutService = new AboutServiceImpl(properties);
    }

    @Test
    public void testGetProjectDetails() {
        when(project.getVersion()).thenReturn("test-version");
        when(project.getLabel()).thenReturn("test-label");

        final AboutInfoDto details = aboutService.getAboutInfo();
        assertEquals("vtest-version", details.getVersion());
        assertEquals("test-label", details.getLabel());
    }
}
