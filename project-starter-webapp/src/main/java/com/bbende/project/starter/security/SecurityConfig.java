package com.bbende.project.starter.security;

import com.bbende.project.starter.security.token.TokenAuthenticationFilter;
import com.bbende.project.starter.security.token.TokenAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.bbende.project.starter.security.SecurityConstants.AUTHORIZATION_BEARER_COOKIE;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] UNAUTHENTICATED_URL_PATTERNS = {
            "/login",
            "/api/token",
            "/fonts/**",
            "/javascript/**",
            "/stylesheets/**"
    };

    private static final String[] CSRF_EXCLUDE_PATTERNS = {
            "/logout",
            "/api/**"
    };

    private static final String LOGIN_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_SUCCESS_URL = "/";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGOUT_SUCCESS_URL = LOGIN_URL + "?logout";

    private final TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    public SecurityConfig(final TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler,
                          final TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationSuccessHandler = tokenAuthenticationSuccessHandler;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers(UNAUTHENTICATED_URL_PATTERNS).permitAll()
                .antMatchers("/**").authenticated()
            .and()
                .formLogin()
                    .loginPage(LOGIN_URL)
                    .failureUrl(LOGIN_FAILURE_URL)
                    .defaultSuccessUrl(LOGIN_SUCCESS_URL)
                    .successHandler(tokenAuthenticationSuccessHandler)
            .and()
                .logout()
                    .logoutUrl(LOGOUT_URL)
                    .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies(AUTHORIZATION_BEARER_COOKIE)
                    .permitAll()
            .and()
                .csrf()
                    .ignoringAntMatchers(CSRF_EXCLUDE_PATTERNS)
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }
}
