package com.bbende.project.starter.test;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.server.Ssl;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
public abstract class WebIT {

    @LocalServerPort
    protected int localPort;

    @Autowired
    protected ServerProperties serverProperties;

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
