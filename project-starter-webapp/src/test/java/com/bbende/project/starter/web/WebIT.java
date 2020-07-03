package com.bbende.project.starter.web;

import com.bbende.project.starter.TestApplication;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.server.Ssl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Base class for web-related IT tests.
 *
 * Tests should call createBaseUrl() to obtain the base url for requests constructed from
 * the server properties, including the server context path.
 *
 * Example: http://localhost:8080/project-starter
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
public abstract class WebIT {

    @LocalServerPort
    protected int localPort;

    @Autowired
    protected ServerProperties serverProperties;

    @Before
    @FlywayTest
    public void cleanDatabase() {
        // ----- NOTE ------
        // The @FlywayTest annotation will cause the DB to be reset and re-migrated before each test
        // which ensures a consistent state even if we commit a test transaction to add/delete data
    }

    protected String createBaseUrl() {
        final Ssl ssl = serverProperties.getSsl();
        final boolean isSecure = ssl != null && ssl.getKeyStore() != null;
        final String protocolSchema = isSecure ? "https" : "http";

        final StringBuilder baseUriBuilder = new StringBuilder()
                .append(protocolSchema)
                .append("://localhost:")
                .append(localPort);

        final ServerProperties.Servlet servlet = serverProperties.getServlet();
        final String serverContextPath = servlet.getContextPath();

        if (!StringUtils.isBlank(serverContextPath)) {
            if (!serverContextPath.startsWith("/")) {
                baseUriBuilder.append("/");
            }
            baseUriBuilder.append(serverContextPath);
        }

        return baseUriBuilder.toString();
    }

}
