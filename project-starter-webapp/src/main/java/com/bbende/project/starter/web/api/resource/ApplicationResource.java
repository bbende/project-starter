package com.bbende.project.starter.web.api.resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

public class ApplicationResource {

    public static final String PROXY_SCHEME_HTTP_HEADER = "X-ProxyScheme";
    public static final String PROXY_HOST_HTTP_HEADER = "X-ProxyHost";
    public static final String PROXY_PORT_HTTP_HEADER = "X-ProxyPort";
    public static final String PROXY_CONTEXT_PATH_HTTP_HEADER = "X-ProxyContextPath";

    public static final String FORWARDED_PROTO_HTTP_HEADER = "X-Forwarded-Proto";
    public static final String FORWARDED_HOST_HTTP_HEADER = "X-Forwarded-Server";
    public static final String FORWARDED_PORT_HTTP_HEADER = "X-Forwarded-Port";
    public static final String FORWARDED_CONTEXT_HTTP_HEADER = "X-Forwarded-Context";

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResource.class);

    @Context
    private HttpServletRequest httpServletRequest;

    @Context
    private UriInfo uriInfo;


    protected URI getBaseUri() {
        final UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        URI uri = uriBuilder.build();
        try {

            // check for proxy settings
            final String scheme = getFirstHeaderValue(PROXY_SCHEME_HTTP_HEADER, FORWARDED_PROTO_HTTP_HEADER);
            final String host = getFirstHeaderValue(PROXY_HOST_HTTP_HEADER, FORWARDED_HOST_HTTP_HEADER);
            final String port = getFirstHeaderValue(PROXY_PORT_HTTP_HEADER, FORWARDED_PORT_HTTP_HEADER);
            String baseContextPath = getFirstHeaderValue(PROXY_CONTEXT_PATH_HTTP_HEADER, FORWARDED_CONTEXT_HTTP_HEADER);

            // if necessary, prepend the context path
            String resourcePath = uri.getPath();
            if (baseContextPath != null) {
                // normalize context path
                if (!baseContextPath.startsWith("/")) {
                    baseContextPath = "/" + baseContextPath;
                }

                if (baseContextPath.endsWith("/")) {
                    baseContextPath = StringUtils.substringBeforeLast(baseContextPath, "/");
                }

                // determine the complete resource path
                resourcePath = baseContextPath + resourcePath;
            }

            // determine the port uri
            int uriPort = uri.getPort();
            if (port != null) {
                if (StringUtils.isWhitespace(port)) {
                    uriPort = -1;
                } else {
                    try {
                        uriPort = Integer.parseInt(port);
                    } catch (final NumberFormatException nfe) {
                        LOGGER.warn(String.format("Unable to parse proxy port HTTP header '%s'. Using port from request URI '%s'.", port, uriPort));
                    }
                }
            }

            // construct the URI
            uri = new URI(
                    (StringUtils.isBlank(scheme)) ? uri.getScheme() : scheme,
                    uri.getUserInfo(),
                    (StringUtils.isBlank(host)) ? uri.getHost() : host,
                    uriPort,
                    resourcePath,
                    uri.getQuery(),
                    uri.getFragment());

        } catch (final URISyntaxException use) {
            throw new UriBuilderException(use);
        }
        return uri;
    }

    protected UriBuilder getBaseUriBuilder() {
        return UriBuilder.fromUri(getBaseUri());
    }

    protected String generateResourceUri(final String... path) {
        final URI baseUri = getBaseUri();
        final URI fullUri = UriBuilder.fromUri(baseUri).segment(path).build();
        return fullUri.toString();
    }

    /**
     * Edit the response headers to indicating no caching.
     *
     * @param response response
     * @return builder
     */
    protected Response.ResponseBuilder noCache(final Response.ResponseBuilder response) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        return response.cacheControl(cacheControl);
    }

    /**
     * Generates an OK response with the specified content.
     *
     * @param entity The entity
     * @return The response to be built
     */
    protected Response.ResponseBuilder generateOkResponse(final Object entity) {
        final Response.ResponseBuilder response = Response.ok(entity);
        return noCache(response);
    }

    /**
     * Generates a 201 Created response with the specified content.
     *
     * @param uri    The URI
     * @param entity entity
     * @return The response to be built
     */
    protected Response.ResponseBuilder generateCreatedResponse(final URI uri, final Object entity) {
        // generate the response builder
        return Response.created(uri).entity(entity);
    }

    /**
     * Returns the value for the first key discovered when inspecting the current request. Will
     * return null if there are no keys specified or if none of the specified keys are found.
     *
     * @param keys http header keys
     * @return the value for the first key found
     */
    private String getFirstHeaderValue(final String... keys) {
        if (keys == null) {
            return null;
        }

        for (final String key : keys) {
            final String value = httpServletRequest.getHeader(key);

            // if we found an entry for this key, return the value
            if (value != null) {
                return value;
            }
        }

        // unable to find any matching keys
        return null;
    }

}