package com.bbende.project.starter.web.api.resource;

import com.bbende.project.starter.security.token.TokenService;
import com.bbende.project.starter.web.api.request.TokenRequest;
import com.bbende.project.starter.web.api.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.bbende.project.starter.security.SecurityConstants.AUTHORIZATION_HEADER;
import static com.bbende.project.starter.security.SecurityConstants.BEARER_PREFIX;

/**
 * Resource for obtaining a token by authenticating via username and password.
 *
 * The token will be submitted on further API requests through the Authorization header.
 */
@Component
@Path("/token")
public class TokenResource extends ApplicationResource {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public TokenResource(final TokenService tokenService,
                         final AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get token",
            description = "Authenticate via username and password and obtain a token",
            tags = {"token"},
            responses = {
                    @ApiResponse(
                            description = "The bearer token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = TokenResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public Response getToken(
            @Valid
            @Parameter(
                    description = "The token request containing the credentials to authenticate",
                    schema = @Schema(implementation = TokenRequest.class),
                    required = true
            )
            final TokenRequest tokenRequest) {

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                tokenRequest.getUsername(), tokenRequest.getPassword());

        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = tokenService.createToken(authentication, false);
        final TokenResponse tokenResponse = new TokenResponse(token);
        return Response.ok(tokenResponse).header(AUTHORIZATION_HEADER, BEARER_PREFIX + token).build();
    }
}
