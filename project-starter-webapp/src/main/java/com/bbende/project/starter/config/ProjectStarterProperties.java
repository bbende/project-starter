package com.bbende.project.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Type-safe access to config from application.properties that starts with "project.starter".
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "project.starter", ignoreUnknownFields = false)
public class ProjectStarterProperties {

    private final Project project;

    public ProjectStarterProperties(final Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public static class Project {

        private final String version;

        private final String label;

        public Project(final String version, final String label) {
            this.version = version;
            this.label = label;
        }

        public String getVersion() {
            return version;
        }

        public String getLabel() {
            return label;
        }

    }
}
