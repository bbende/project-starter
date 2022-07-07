package com.bbende.project.starter.test;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.Map;

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
        client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .build();
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

    protected Response doFormLogin(final String username, final String password) {
        final String loginUrl = createBaseUrl() + "/login";
        final WebTarget loginTarget = client.target(loginUrl);

        final FormDataMultiPart multipart = new FormDataMultiPart();
        multipart.field("username", username);
        multipart.field("password", password);

        final Response response = loginTarget.request()
                .post(Entity.entity(multipart, multipart.getMediaType()));

        final Map<String, NewCookie> cookies = response.getCookies();
        final MultivaluedMap<String, Object> headers = response.getHeaders();

        return response;
    }
}
