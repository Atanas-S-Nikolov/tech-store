package com.techstore.service.user;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.user.UserServiceException;
import com.techstore.model.response.JWTResponse;
import com.techstore.utils.converter.ModelConverter;
import com.techstore.model.User;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.enums.UserRole;
import com.techstore.repository.IUserRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static com.techstore.constants.JWTConstants.BEARER;
import static com.techstore.constants.JWTConstants.ROLES_CLAIM;
import static com.techstore.utils.JWTUtils.*;
import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toModel;
import static com.techstore.model.enums.UserRole.ROLE_ADMIN;
import static com.techstore.model.enums.UserRole.ROLE_CUSTOMER;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class UserService extends AbstractUserService {
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final String jwtSecret;

    public UserService(IUserRepository repository, PasswordEncoder passwordEncoder, String jwtSecret) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public User createCustomer(User user) {
        return createUserWithRole(user, ROLE_CUSTOMER);
    }

    @Override
    public User createAdmin(User user) {
        return createUserWithRole(user, ROLE_ADMIN);
    }

    @Override
    public Collection<User> getAllUsers() {
        return executeDBCall(() -> repository.findAll().stream()
                .map(ModelConverter::toModel)
                .collect(toList()));
    }

    @Override
    public User updateUser(User user) {
        UserEntity existingEntity = findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        UserEntity entity = toEntity(user);
        entity.setId(existingEntity.getId());

        String newPassword = user.getNewPassword();
        if (nonNull(newPassword) && !newPassword.trim().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(newPassword));
        }

        if (existingEntity.getRole() != entity.getRole()) {
            throw new UserConstraintViolationException("Cannot overwrite user role");
        }

        return toModel(executeDBCall(() -> repository.save(entity)));
    }

    @Override
    public void deleteUser(String username, String password) {
        UserEntity entity = findUserByUsernameAndPassword(username, password);
        executeDBCall(() -> repository.delete(entity));
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

    private User createUserWithRole(User user, UserRole role) {
        UserEntity entity = toEntity(user);
        entity.setRole(role);
        entity.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        return toModel(executeDBCall(() -> repository.save(entity)));
    }

    private UserEntity findUserByUsernameAndPassword(String username, String password) {
        UserEntity entity = findUserByUsername(username);
        if (!passwordEncoder.matches(password, entity.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return entity;
    }

    private UserEntity findUserByUsername(String username) {
        return executeDBCall(() -> repository.findUserByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(format("User with username '%s' is not found", username)));
    }

    private <T> T executeDBCall(Supplier<T> supplier) {
        try{
            return supplier.get();
        } catch (DataIntegrityViolationException | ConstraintViolationException violationException) {
            throw new UserConstraintViolationException("User constraint violation", violationException);
        } catch (DataAccessException dataAccessException) {
            throw new UserServiceException("Error while connecting the database", dataAccessException);
        }
    }

    private void executeDBCall(Runnable runnable) {
        try {
            runnable.run();
        } catch (DataIntegrityViolationException | ConstraintViolationException violationException) {
            throw new UserConstraintViolationException("User constraint violation", violationException);
        } catch (DataAccessException dataAccessException) {
            throw new UserServiceException("Error while connecting the database", dataAccessException);
        }
    }
}
