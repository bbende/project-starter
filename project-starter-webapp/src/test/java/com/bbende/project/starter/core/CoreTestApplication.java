package com.bbende.project.starter.core;

import com.bbende.project.starter.core.config.ProjectStarterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Spring Boot Application for running integration tests within project-starter-core.
 *
 * This class must be at the base package level (com.bbende.project.starter) so that it can
 * find beans in all sub-packages.
 */
@SpringBootApplication
@EnableConfigurationProperties({ProjectStarterProperties.class})
public class CoreTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreTestApplication.class, args);
    }

}
