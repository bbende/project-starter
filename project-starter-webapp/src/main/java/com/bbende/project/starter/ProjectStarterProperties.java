package com.bbende.project.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Type-safe access to config from application.properties that starts with "project.starter".
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "project.starter", ignoreUnknownFields = false)
public class ProjectStarterProperties {

    private final Project project;
    private final Security security;

    public ProjectStarterProperties(final Project project, final Security security) {
        this.project = project;
        this.security = security;
    }

    public Project getProject() {
        return project;
    }

    public Security getSecurity() {
        return security;
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

    public static class Security {

        private final Token token;

        public Security(final Token token) {
            this.token = token;
        }

        public Token getToken() {
            return token;
        }
    }

    public static class Token {
        private final String base64Secret;
        private final int validityInSeconds;
        private final int validityInSecondsForRememberMe;

        public Token(
                final String base64Secret,
                final int validityInSeconds,
                final int validityInSecondsForRememberMe) {
            this.base64Secret = base64Secret;
            this.validityInSeconds = validityInSeconds;
            this.validityInSecondsForRememberMe = validityInSecondsForRememberMe;
        }

        public String getBase64Secret() {
            return base64Secret;
        }

        public int getValidityInSeconds() {
            return validityInSeconds;
        }

        public int getValidityInSecondsForRememberMe() {
            return validityInSecondsForRememberMe;
        }
    }
}
