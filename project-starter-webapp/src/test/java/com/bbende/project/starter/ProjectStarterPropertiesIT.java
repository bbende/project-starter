package com.bbende.project.starter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ProjectStarterPropertiesIT {

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
