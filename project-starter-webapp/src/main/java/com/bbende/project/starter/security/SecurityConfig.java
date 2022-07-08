package com.bbende.project.starter.security;

import com.bbende.project.starter.security.cookie.CookieService;
import com.bbende.project.starter.security.token.TokenAuthenticationFilter;
import com.bbende.project.starter.security.token.TokenAuthenticationSuccessHandler;
import com.bbende.project.starter.security.token.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.bbende.project.starter.security.SecurityConstants.AUTHORIZATION_BEARER_COOKIE;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    public SecurityConfig(final TokenService tokenService,
                          final CookieService cookieService,
                          final UserDetailsService userDetailsService) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userDetailsService = userDetailsService;
        this.tokenAuthenticationSuccessHandler
                = new TokenAuthenticationSuccessHandler(tokenService, cookieService);
        this.tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenService);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * Expose the AuthenticationManager as a bean so that it can be injected into TokenResource.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/api/token", "/fonts/**", "/javascript/**", "/stylesheets/**").permitAll()
                .antMatchers("/**").authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/")
                .successHandler(tokenAuthenticationSuccessHandler)
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies(AUTHORIZATION_BEARER_COOKIE)
                .permitAll()
            .and()
                .csrf()
                .ignoringAntMatchers("/logout", "/api/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
