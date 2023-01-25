package com.techstore.service.user;

import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserNotFoundException;
import com.techstore.model.User;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.enums.UserRole;
import com.techstore.repository.IUserRepository;
import com.techstore.service.cart.ICartService;
import com.techstore.utils.converter.ModelConverter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.function.Supplier;

import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toModel;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserService implements IUserService {
    private final IUserRepository repository;
    private final ICartService cartService;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository repository, ICartService cartService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User createUserWithRole(User user, UserRole role) {
        UserEntity entity = toEntity(user);
        entity.setRole(role);
        entity.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        entity.setCart(cartService.createDefaultCart());
        return toModel(executeDBCall(() -> repository.save(entity)));
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

    @Transactional
    @Override
    public void deleteUser(String username, String password) {
        UserEntity entity = findUserByUsernameAndPassword(username, password);
        cartService.deleteCart(username);
        executeDBCall(() -> repository.delete(entity));
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
                .orElseThrow(() -> new UserNotFoundException(format("User with username '%s' is not found", username)));
    }

    private <T> T executeDBCall(Supplier<T> supplier) {
        try{
            return supplier.get();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new UserConstraintViolationException("User constraint violation", dataIntegrityViolationException);
        }
    }

    private void executeDBCall(Runnable runnable) {
        try {
            runnable.run();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new UserConstraintViolationException("User constraint violation", dataIntegrityViolationException);
        }
    }
}
