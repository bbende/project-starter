package com.bbende.project.starter.config;


import com.bbende.project.starter.web.api.resource.ApplicationResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Collection;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    @Autowired
    public JerseyConfig(
            final Collection<ApplicationResource> resources,
            final Collection<ExceptionMapper> exceptionMappers) {

        // register resources
        resources.forEach(r -> register(r.getClass()));

        // register exception mappers
        exceptionMappers.forEach(em -> register(em.getClass()));

        // include bean validation errors in response
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        // since we run Jersey as a filter, forward any 404s so requests continue on
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
}
