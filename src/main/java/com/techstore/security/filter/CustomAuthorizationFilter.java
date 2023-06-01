package com.techstore.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.techstore.constants.ApiConstants.FULL_REFRESH_TOKEN_URL;
import static com.techstore.constants.ApiConstants.LOGIN_URL;
import static com.techstore.constants.JWTConstants.BEARER;
import static com.techstore.constants.JWTConstants.ROLES_CLAIM;
import static com.techstore.utils.auth.AuthUtils.convertStringsToAuthorities;
import static com.techstore.utils.JWTUtils.generateAlgorithmWithSecret;
import static com.techstore.utils.JWTUtils.verifyToken;
import static com.techstore.utils.converter.JsonConverter.toJson;
import static com.techstore.model.response.builder.ErrorResponseBuilder.buildErrorResponse;

import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
        boolean isLoginUrl = requestPath.equals(LOGIN_URL);
        boolean isRefreshTokenUrl = requestPath.equals(FULL_REFRESH_TOKEN_URL);

        if (!(isLoginUrl || isRefreshTokenUrl)) {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (nonNull(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
                try {
                    String token = authorizationHeader.substring(BEARER.length());
                    Algorithm algorithm = generateAlgorithmWithSecret(jwtSecret);
                    DecodedJWT decodedJWT = verifyToken(token, algorithm);
                    String username = decodedJWT.getSubject();
                    List<String> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(String.class);
                    List<GrantedAuthority> authorities = convertStringsToAuthorities(roles);
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (Exception exception) {
                    response.setStatus(SC_FORBIDDEN);
                    toJson(response.getOutputStream(), buildErrorResponse(FORBIDDEN, exception.getMessage()));
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
