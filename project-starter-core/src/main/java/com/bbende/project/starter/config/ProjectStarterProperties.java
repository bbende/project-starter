package com.bbende.project.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Type-safe access to config from application.properties that starts with "project.starter".
 */
@ConfigurationProperties(prefix = "project.starter", ignoreUnknownFields = false)
public class ProjectStarterProperties {

    private final Project project = new Project();

    public Project getProject() {
        return project;
    }

    public static class Project {

        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
