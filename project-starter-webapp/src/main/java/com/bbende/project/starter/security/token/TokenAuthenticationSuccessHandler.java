package com.bbende.project.starter.security.token;

import com.bbende.project.starter.security.cookie.CookieService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bbende.project.starter.security.SecurityConstants.AUTHORIZATION_BEARER_COOKIE;

/**
 * Handler for a successful form-login which will create a token and add it to the response
 * in a Cookie which will be submitted back on future requests.
 */
@Component
public class TokenAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final CookieService cookieService;

    public TokenAuthenticationSuccessHandler(final TokenService tokenService, final CookieService cookieService) {
        this.tokenService = tokenService;
        this.cookieService = cookieService;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws ServletException, IOException {
        final String token = tokenService.createToken(authentication, false);
        cookieService.addSessionCookie(request, response, AUTHORIZATION_BEARER_COOKIE, token);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
