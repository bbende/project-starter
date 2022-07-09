package com.bbende.project.starter.web.api.response;

public class TokenResponse {

    private final String token;

    private TokenResponse() {
        token = null;
    }

    public TokenResponse(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
