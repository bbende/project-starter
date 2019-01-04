package com.bbende.project.starter.config;

import com.bbende.project.starter.IntegrationTestApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = IntegrationTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestProjectStarterProperties {

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
