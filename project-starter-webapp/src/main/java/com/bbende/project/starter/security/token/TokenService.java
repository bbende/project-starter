package com.bbende.project.starter.security.token;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String createToken(Authentication authentication, boolean rememberMe);

    Authentication getAuthentication(String token);

    boolean validateToken(String authToken);

}
