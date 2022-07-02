package com.bbende.project.starter.config;

import com.bbende.project.starter.component.user.UserService;
import com.bbende.project.starter.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(final UserService userService) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userDetailsService = new UserDetailsServiceImpl(userService);
    }

    @Bean
    @Primary
    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    @Bean
    @Primary
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(getUserDetailsService())
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/fonts/**", "/javascript/**", "/stylesheets/**").permitAll()
                .antMatchers("/**").authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/")
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            .and()
                .csrf()
                .ignoringAntMatchers("/logout")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
