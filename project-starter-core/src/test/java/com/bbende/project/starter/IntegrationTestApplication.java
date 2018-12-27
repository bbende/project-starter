package com.bbende.project.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application for running integration tests within project-starter-core.
 *
 * This class must be at the base package level (com.bbende.project.starter) so that it can
 * find beans in all sub-packages.
 */
@SpringBootApplication
public class IntegrationTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationTestApplication.class, args);
    }

}
