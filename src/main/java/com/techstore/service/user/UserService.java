package com.techstore.service.user;

import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.InvalidCredentialsException;
import com.techstore.exception.user.UserServiceException;
import com.techstore.model.ModelConverter;
import com.techstore.model.User;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.enums.UserRole;
import com.techstore.repository.IUserRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.techstore.model.ModelConverter.toEntity;
import static com.techstore.model.ModelConverter.toModel;
import static com.techstore.model.enums.UserRole.ADMIN;
import static com.techstore.model.enums.UserRole.CUSTOMER;
import static java.util.Objects.nonNull;

public class UserService implements IUserService {
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createCustomer(User user) {
        return createUserWithRole(user, CUSTOMER);
    }

    @Override
    public User createAdmin(User user) {
        return createUserWithRole(user, ADMIN);
    }

    @Override
    public User getUser(String username, String password) {
        return toModel(findUserByUsernameAndPassword(username, password));
    }

    @Override
    public Collection<User> getAllUsers() {
        return executeDBCall(() -> repository.findAll().stream()
                .map(ModelConverter::toModel)
                .collect(Collectors.toList()));
    }

    @Override
    public User updateUser(User user) {
        UserEntity existingEntity = findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        UserEntity entity = toEntity(user);
        entity.setId(existingEntity.getId());

        String newPassword = user.getNewPassword();
        if (nonNull(newPassword) && !newPassword.isEmpty()) {
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

    private User createUserWithRole(User user, UserRole role) {
        UserEntity entity = toEntity(user);
        entity.setRole(role);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        return toModel(executeDBCall(() -> repository.save(entity)));
    }

    private UserEntity findUserByUsernameAndPassword(String username, String password) {
        Optional<UserEntity> optionalEntity = executeDBCall(() -> repository.findUserByUsername(username));

        if (optionalEntity.isPresent()) {
            UserEntity entity = optionalEntity.get();
            if (passwordEncoder.matches(password, entity.getPassword())) {
                return entity;
            }
        }

        throw new InvalidCredentialsException("Invalid credentials or user is not registered");
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
