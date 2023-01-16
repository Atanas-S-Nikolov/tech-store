package com.techstore.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.techstore.model.dto.AuthenticationDto;
import com.techstore.model.response.JWTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.techstore.utils.JWTUtils.convertAuthoritiesToStrings;
import static com.techstore.utils.JWTUtils.generateAlgorithmWithSecret;
import static com.techstore.utils.JWTUtils.generateAccessToken;
import static com.techstore.utils.JWTUtils.generateRefreshToken;
import static com.techstore.utils.converter.JsonConverter.toJson;

import static java.lang.System.lineSeparator;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthenticationDto authenticationDto = new AuthenticationDto();

        try {
            String jsonString = request.getReader().lines().collect(Collectors.joining(lineSeparator()));
            authenticationDto = toJson(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(), authenticationDto.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getPrincipal().toString();
        Algorithm algorithm = generateAlgorithmWithSecret(jwtSecret);
        List<String> roles = convertAuthoritiesToStrings(authentication);

        String accessToken = generateAccessToken(username, roles, algorithm);
        String refreshToken = generateRefreshToken(username, algorithm);
//        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
//        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
//        accessTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setHttpOnly(true);
//        response.addCookie(accessTokenCookie);
//        response.addCookie(refreshTokenCookie);
        response.setContentType(APPLICATION_JSON_VALUE);

        toJson(response.getOutputStream(), new JWTResponse(accessToken, refreshToken));
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
