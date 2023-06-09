package com.techstore.service.access;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.exception.user.TokenException;
import com.techstore.exception.user.UserNotFoundException;
import com.techstore.model.entity.RegisterConfirmationTokenEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.GenericResponse;
import com.techstore.model.response.JWTResponse;
import com.techstore.repository.IRegisterConfirmationTokenRepository;
import com.techstore.repository.IUserRepository;
import com.techstore.service.jwt.IJWTService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

import static com.techstore.constants.JWTConstants.BEARER;
import static com.techstore.constants.JWTConstants.ROLES_CLAIM;
import static com.techstore.utils.auth.AuthUtils.convertStringsToAuthorities;
import static com.techstore.utils.JWTUtils.generateAccessToken;
import static com.techstore.utils.JWTUtils.generateAlgorithmWithSecret;
import static com.techstore.utils.JWTUtils.verifyToken;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AccessControlService implements UserDetailsService, IJWTService {
    private final String jwtSecret;
    private final IUserRepository userRepository;
    private final IRegisterConfirmationTokenRepository registerConfirmationTokenRepository;

    public AccessControlService(String jwtSecret, IUserRepository userRepository, IRegisterConfirmationTokenRepository registerConfirmationTokenRepository) {
        this.jwtSecret = jwtSecret;
        this.userRepository = userRepository;
        this.registerConfirmationTokenRepository = registerConfirmationTokenRepository;
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

    @Transactional
    public GenericResponse confirmRegistration(String token) {
        RegisterConfirmationTokenEntity tokenEntity = findRegisterConfirmationTokenEntity(token);
        UserEntity userEntity = findUserEntityByRegistrationConfirmationToken(tokenEntity);
        checkTokenExpiration(tokenEntity);
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        registerConfirmationTokenRepository.delete(tokenEntity);
        return new GenericResponse("Registration is confirmed successfully");
    }

    private UserEntity findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User with username '%s' is not found", username)));
    }

    private UserEntity findUserEntityByRegistrationConfirmationToken(RegisterConfirmationTokenEntity tokenEntity) {
        return userRepository.findUserByRegisterConfirmationToken(tokenEntity)
                .orElseThrow(() -> new UserNotFoundException(format("User with register confirmation token '%s' is not found", tokenEntity.getToken())));
    }

    private RegisterConfirmationTokenEntity findRegisterConfirmationTokenEntity(String token) {
        return registerConfirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException(format("Token: %s is not found", token)));
    }

    private void checkTokenExpiration(RegisterConfirmationTokenEntity tokenEntity) {
        long currentTimeMs = System.currentTimeMillis();
        long tokenEntityExpirationMs = tokenEntity.getExpirationMs();
        if (currentTimeMs > tokenEntityExpirationMs) {
            throw new TokenException(format("Token: %s is expired",tokenEntity.getToken()));
        }
    }
}
