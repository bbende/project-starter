package com.bbende.project.starter.security;

public interface SecurityConstants {

    // TODO Switch to more correct secure Cookie name when there is a way to run the
    // application locally over https, otherwise browser won't sent the secure Cookie
    //String AUTHORIZATION_BEARER_COOKIE = "__Secure-Authorization-Bearer";

    String AUTHORIZATION_BEARER_COOKIE = "Authorization-Bearer";
    String AUTHORIZATION_HEADER = "Authorization";
    String BEARER_PREFIX = "Bearer ";
}
