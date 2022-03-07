package com.bbende.project.starter.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Base class for REST API tests.
 *
 * Tests should call createResourceUrl(relativeResourcePath) to obtain the path to a resource.
 *
 * Example: http://localhost:8080/project-starter/{relativeResourcePath}
 */
public abstract class RestIT extends WebIT {

    @Autowired
    protected JerseyProperties jerseyProperties;

    protected Client client;
    protected String apiContextPath;

    @BeforeEach
    public void setupRestIT() {
        client = ClientBuilder.newBuilder().build();
        apiContextPath = jerseyProperties.getApplicationPath();
    }

    @AfterEach
    public void cleanup() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                // Nothing to do
            }
        }
    }

    protected String createResourceUrl(String relativeResourcePath) {
        if (relativeResourcePath == null) {
            throw new IllegalArgumentException("Resource path cannot be null");
        }

        final StringBuilder baseUriBuilder = new StringBuilder(createBaseUrl()).append(apiContextPath);

        if (!relativeResourcePath.startsWith("/")) {
            baseUriBuilder.append('/');
        }
        baseUriBuilder.append(relativeResourcePath);

        return baseUriBuilder.toString();
    }

}
