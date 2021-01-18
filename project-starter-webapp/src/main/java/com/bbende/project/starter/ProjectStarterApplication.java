package com.bbende.project.starter;

import com.bbende.project.starter.config.ProjectStarterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ProjectStarterProperties.class})
public class ProjectStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectStarterApplication.class, args);
    }

}
