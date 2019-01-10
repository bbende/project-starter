package com.bbende.project.starter;

import com.bbende.project.starter.config.ProjectStarterProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableConfigurationProperties({ProjectStarterProperties.class})
public class ProjectStarterApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectStarterApplication.class);

    @Autowired
    private Environment env;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private JerseyProperties jerseyProperties;

    public static void main(String[] args) {
        SpringApplication.run(ProjectStarterApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        final Ssl ssl = serverProperties.getSsl();
        final boolean isSecure = ssl != null && ssl.getKeyStore() != null;
        final String scheme = isSecure ? "https" : "http";

        String host;
        final InetAddress serverAddress = serverProperties.getAddress();
        if (serverAddress == null) {
            try {
                host = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                host = "localhost";
            }
        } else {
            host = serverAddress.getCanonicalHostName();
        }

        final int port = serverProperties.getPort();
        final String serverContextPath = serverProperties.getServlet().getContextPath();
        final String jerseyContextPath = jerseyProperties.getApplicationPath();

        final StringBuilder baseUrl = new StringBuilder(scheme).append("://").append(host).append(":").append(port);
        if (!StringUtils.isEmpty(serverContextPath)) {
            baseUrl.append(serverContextPath);
        } else {
            baseUrl.append("/");
        }

        final String appName = env.getProperty("spring.application.name");
        final String uiUrl = baseUrl.toString();
        final String apiUrl = baseUrl.append(jerseyContextPath).toString();

        LOGGER.info("--------------------------------------------------------------------");
        LOGGER.info("Application '{}' has started and is available at the following URLs:", new Object[]{appName});
        LOGGER.info("- UI:\t {}", new Object[]{uiUrl});
        LOGGER.info("- API:\t {}", new Object[]{apiUrl});
        LOGGER.info("--------------------------------------------------------------------");
    }
}
