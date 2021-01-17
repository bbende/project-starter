package com.bbende.project.starter.core.config;

import com.bbende.project.starter.core.CoreTestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        classes = CoreTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class ProjectStarterPropertiesTest {

    @Autowired
    private ProjectStarterProperties properties;

    @Test
    public void testProjectVersionIsNotEmpty() {
        Assertions.assertNotNull(properties);
        Assertions.assertNotNull(properties.getProject());
        Assertions.assertNotNull(properties.getProject().getVersion());
        Assertions.assertFalse(properties.getProject().getVersion().isEmpty());
    }
}
