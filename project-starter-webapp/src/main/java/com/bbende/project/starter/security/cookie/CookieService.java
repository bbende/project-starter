package com.bbende.project.starter.security.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieService {

    void addCookie(HttpServletRequest request, HttpServletResponse response,
                   String cookieName, String cookieValue);

    void addSessionCookie(HttpServletRequest request, HttpServletResponse response,
                   String cookieName, String cookieValue);

}
