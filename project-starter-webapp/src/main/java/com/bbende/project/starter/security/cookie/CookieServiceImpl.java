package com.bbende.project.starter.security.cookie;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.Duration;

@Service
class CookieServiceImpl implements CookieService {

    private static final Duration MAX_AGE_SESSION = Duration.ofSeconds(-1);
    private static final Duration MAX_AGE_REMOVE = Duration.ZERO;
    private static final Duration MAX_AGE_STANDARD = Duration.ofSeconds(60);

    private static final String DEFAULT_COOKIE_PATH = "/";
    private static final String SAME_SITE_STRICT = "Strict";

    private final boolean secured;

    public CookieServiceImpl(final ServerProperties serverProperties) {
        final Ssl sslProperties = serverProperties.getSsl();
        this.secured = sslProperties != null && sslProperties.isEnabled();
    }

    @Override
    public void addCookie(final HttpServletRequest request, final HttpServletResponse response,
                          final String cookieName, final String cookieValue) {
        final ResponseCookie responseCookie = getResponseCookieBuilder(request, cookieName, cookieValue, MAX_AGE_STANDARD).build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    @Override
    public void addSessionCookie(final HttpServletRequest request, final HttpServletResponse response,
                                 final String cookieName, final String cookieValue) {
        final ResponseCookie responseCookie =
                getResponseCookieBuilder(request, cookieName, cookieValue, MAX_AGE_SESSION)
                        .sameSite(SAME_SITE_STRICT)
                        .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private ResponseCookie.ResponseCookieBuilder getResponseCookieBuilder(final HttpServletRequest request, final String cookieName,
                                                                          final String cookieValue, final Duration maxAge) {
        final URI requestUri = URI.create(request.getRequestURI());
        return ResponseCookie.from(cookieName, cookieValue)
                .path(getCookiePath(request))
                .domain(requestUri.getHost())
                .secure(secured)
                .httpOnly(true)
                .maxAge(maxAge);
    }

    private String getCookiePath(final HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        if (StringUtils.isEmpty(contextPath)) {
            return DEFAULT_COOKIE_PATH;
        } else {
            return contextPath.startsWith("/") ? contextPath : "/" + contextPath;
        }
    }
}
