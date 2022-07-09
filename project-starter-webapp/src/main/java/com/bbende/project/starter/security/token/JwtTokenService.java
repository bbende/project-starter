package com.bbende.project.starter.security.token;

import com.bbende.project.starter.ProjectStarterProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Implementation of TokenService based on JWTs.
 */
@Service
class JwtTokenService implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenService.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private final Key key;
    private final JwtParser jwtParser;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    public JwtTokenService(final ProjectStarterProperties properties) {
        final String secret = properties.getSecurity().getToken().getBase64Secret();
        if (ObjectUtils.isEmpty(secret)) {
            throw new IllegalStateException("Token secret must be set");
        }

        final byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

        tokenValidityInMilliseconds =
                1000L * properties.getSecurity().getToken().getValidityInSeconds();
        tokenValidityInMillisecondsForRememberMe =
                1000L * properties.getSecurity().getToken().getValidityInSecondsForRememberMe();
    }

    @Override
    public String createToken(final Authentication authentication, final boolean rememberMe) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        final long now = (new Date()).getTime();
        final Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    @Override
    public Authentication getAuthentication(final String token) {
        final Claims claims = jwtParser.parseClaimsJws(token).getBody();

        final Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        final User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.trace(INVALID_JWT_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            LOGGER.trace(INVALID_JWT_TOKEN, e);
        } catch (MalformedJwtException e) {
            LOGGER.trace(INVALID_JWT_TOKEN, e);
        } catch (SignatureException e) {
            LOGGER.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Token validation error {}", e.getMessage());
        }

        return false;
    }
}
