package com.bbende.project.starter.core.config;

import com.bbende.project.starter.core.CoreTestApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = CoreTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class ProjectStarterPropertiesTest {

    @Autowired
    private ProjectStarterProperties properties;

    @Test
    public void testProjectVersionIsNotEmpty() {
        Assert.assertNotNull(properties);
        Assert.assertNotNull(properties.getProject());
        Assert.assertNotNull(properties.getProject().getVersion());
        Assert.assertFalse(properties.getProject().getVersion().isEmpty());
    }
}
