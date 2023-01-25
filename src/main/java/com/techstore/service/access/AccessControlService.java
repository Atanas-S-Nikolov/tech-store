package com.techstore.service.access;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.JWTResponse;
import com.techstore.repository.IUserRepository;
import com.techstore.service.jwt.IJWTService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.techstore.constants.JWTConstants.BEARER;
import static com.techstore.constants.JWTConstants.ROLES_CLAIM;
import static com.techstore.utils.JWTUtils.convertStringsToAuthorities;
import static com.techstore.utils.JWTUtils.generateAccessToken;
import static com.techstore.utils.JWTUtils.generateAlgorithmWithSecret;
import static com.techstore.utils.JWTUtils.verifyToken;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AccessControlService implements UserDetailsService, IJWTService {
    private final IUserRepository repository;
    private final String jwtSecret;

    public AccessControlService(IUserRepository repository, String jwtSecret) {
        this.repository = repository;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = findUserByUsername(username);
        List<GrantedAuthority> authorities = convertStringsToAuthorities(singletonList(entity.getRole().getValue()));
        return new org.springframework.security.core.userdetails.User(username, entity.getPassword(), authorities);
    }

    @Override
    public JWTResponse refreshToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        JWTResponse jwtResponse = new JWTResponse();
        if (nonNull(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            try {
                String refreshToken = authorizationHeader.substring(BEARER.length());
                Algorithm algorithm = generateAlgorithmWithSecret(jwtSecret);
                DecodedJWT decodedJWT = verifyToken(refreshToken, algorithm);
                String username = decodedJWT.getSubject();
                List<String> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(String.class);
                jwtResponse = new JWTResponse(generateAccessToken(username, roles, algorithm), refreshToken);
            } catch (Exception e) {
                throw new InvalidJWTException("Refresh token is invalid", e);
            }
        }
        return jwtResponse;
    }

    private UserEntity findUserByUsername(String username) {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User with username '%s' is not found", username)));
    }
}
