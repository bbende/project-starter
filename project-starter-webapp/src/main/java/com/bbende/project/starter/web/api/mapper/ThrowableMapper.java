package com.bbende.project.starter.web.api.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps unknown node exceptions into client responses.
 */
@Component
@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(ThrowableMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        logger.error(String.format("An unexpected error has occurred: %s. Returning %s response.", exception, Response.Status.INTERNAL_SERVER_ERROR), exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error has occurred. Please check the logs for additional details.").type("text/plain").build();
    }

}
