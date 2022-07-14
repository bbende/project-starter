package com.bbende.project.starter.security.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.bbende.project.starter.security.SecurityConstants.AUTHORIZATION_BEARER_COOKIE;
import static com.bbende.project.starter.security.SecurityConstants.AUTHORIZATION_HEADER;
import static com.bbende.project.starter.security.SecurityConstants.BEARER_PREFIX;

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final TokenService tokenService;

    public TokenAuthenticationFilter(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final String token = resolveToken(httpServletRequest);
        if (StringUtils.hasText(token) && tokenService.validateToken(token)) {
            final Authentication authentication = tokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Resolve Bearer Token from HTTP Request checking Authorization Header then Authorization Cookie when found
     *
     * @param request HTTP Servlet Request
     * @return Bearer Token or null when not found
     */
    public String resolveToken(final HttpServletRequest request) {
        final String header = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        } else {
            final Cookie cookie = WebUtils.getCookie(request, AUTHORIZATION_BEARER_COOKIE);
            if (cookie == null) {
                LOGGER.trace("Bearer Token not found in Header or Cookie");
            } else {
                return cookie.getValue();
            }
        }

        return null;
    }

}
