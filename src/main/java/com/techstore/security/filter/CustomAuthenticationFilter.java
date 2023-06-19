package com.techstore.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.techstore.exception.auth.CustomAuthenticationException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.model.dto.AuthenticationDto;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.JWTResponse;
import com.techstore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.techstore.utils.JWTUtils.generateAlgorithmWithSecret;
import static com.techstore.utils.JWTUtils.generateAccessToken;
import static com.techstore.utils.JWTUtils.generateRefreshToken;
import static com.techstore.utils.auth.AuthUtils.convertAuthoritiesToStrings;
import static com.techstore.utils.converter.JsonConverter.toJson;

import static java.lang.System.lineSeparator;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    private IUserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthenticationDto authenticationDto = new AuthenticationDto();

        try {
            String jsonString = request.getReader().lines().collect(Collectors.joining(lineSeparator()));
            authenticationDto = toJson(jsonString, AuthenticationDto.class);
        } catch (IOException e) {
            throw new CustomAuthenticationException("Authentication failed", e);
        }
        String username = authenticationDto.getUsername();
        String password = authenticationDto.getPassword();

        checkIfUserIsEnabled(username);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
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

        try {
            toJson(response.getOutputStream(), new JWTResponse(accessToken, refreshToken));
        } catch (IOException e) {
            throw new CustomAuthenticationException("Failed to send authentication response", e);
        }
    }

    //TODO: Implement failure authentication handler

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkIfUserIsEnabled(String username) {
        UserEntity entity = userRepository.findUserByUsername(username).orElse(new UserEntity());
        if (!entity.isEnabled()) {
            throw new UserConstraintViolationException("User is not enabled");
        }
    }
}
