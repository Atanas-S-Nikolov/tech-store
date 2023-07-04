package com.techstore.service.access;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.techstore.exception.auth.InvalidJWTException;
import com.techstore.exception.user.TokenException;
import com.techstore.exception.user.UserNotFoundException;
import com.techstore.model.dto.ResetPasswordDto;
import com.techstore.model.entity.PasswordResetTokenEntity;
import com.techstore.model.entity.RegisterConfirmationTokenEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.GenericResponse;
import com.techstore.model.response.JWTResponse;
import com.techstore.repository.IPasswordResetTokenRepository;
import com.techstore.repository.IRegisterConfirmationTokenRepository;
import com.techstore.repository.IUserRepository;
import com.techstore.service.jwt.IJWTService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AccessControlService(String jwtSecret, IUserRepository userRepository, IRegisterConfirmationTokenRepository registerConfirmationTokenRepository,
                                IPasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder) {
        this.jwtSecret = jwtSecret;
        this.userRepository = userRepository;
        this.registerConfirmationTokenRepository = registerConfirmationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
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
        checkTokenExpiration(tokenEntity.getExpirationMs());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        registerConfirmationTokenRepository.delete(tokenEntity);
        return new GenericResponse("Registration is confirmed successfully");
    }

    @Transactional
    public GenericResponse resetPassword(ResetPasswordDto resetPasswordDto) {
        PasswordResetTokenEntity tokenEntity = findPasswordResetTokenEntity(resetPasswordDto.getToken());
        try {
            UserEntity userEntity = findUserEntityByPasswordResetToken(tokenEntity);
            checkTokenExpiration(tokenEntity.getExpirationMs());
            userEntity.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword().trim()));
            userRepository.save(userEntity);
        } finally {
            passwordResetTokenRepository.delete(tokenEntity);
        }
        return new GenericResponse("Password has been changed");
    }

    private UserEntity findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User with username '%s' is not found", username)));
    }

    private UserEntity findUserEntityByRegistrationConfirmationToken(RegisterConfirmationTokenEntity tokenEntity) {
        return userRepository.findUserByRegisterConfirmationToken(tokenEntity)
                .orElseThrow(() -> new UserNotFoundException("User with this register confirmation token is not found"));
    }

    private UserEntity findUserEntityByPasswordResetToken(PasswordResetTokenEntity tokenEntity) {
        return userRepository.findUserByPasswordResetToken(tokenEntity)
                .orElseThrow(() -> new UserNotFoundException("User with this password reset token is not found"));
    }

    private RegisterConfirmationTokenEntity findRegisterConfirmationTokenEntity(String token) {
        return registerConfirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException("Token is not found"));
    }

    private PasswordResetTokenEntity findPasswordResetTokenEntity(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException("Token is not found"));
    }

    private void checkTokenExpiration(long tokenExpirationMs) {
        if (System.currentTimeMillis() > tokenExpirationMs) {
            throw new TokenException("Token is expired");
        }
    }
}
