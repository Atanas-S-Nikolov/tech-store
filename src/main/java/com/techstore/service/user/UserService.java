package com.techstore.service.user;

import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserNotFoundException;
import com.techstore.model.User;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.enums.UserRole;
import com.techstore.repository.IUserRepository;
import com.techstore.service.cart.ICartService;
import com.techstore.service.favorites.IFavoritesService;
import com.techstore.utils.converter.ModelConverter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.Collection;

import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toModel;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserService implements IUserService {
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ICartService cartService;
    private final IFavoritesService favoritesService;

    public UserService(IUserRepository repository, PasswordEncoder passwordEncoder, ICartService cartService, IFavoritesService favoritesService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
        this.favoritesService = favoritesService;
    }

    @Transactional
    @Override
    public User createUserWithRole(User user, UserRole role) {
        UserEntity entity = toEntity(user);
        entity.setRole(role);
        entity.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        entity.setCart(cartService.createDefaultCart());
        entity.setFavorite(favoritesService.createDefaultFavorites());
        return toModel(repository.save(entity));
    }

    @Override
    public Collection<User> getAllUsers() {
        return repository.findAll().stream().map(ModelConverter::toModel).collect(toList());
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

        return toModel(repository.save(entity));
    }

    @Transactional
    @Override
    public void deleteUser(String username, String password) {
        UserEntity entity = findUserByUsernameAndPassword(username, password);
        cartService.deleteCart(username);
        favoritesService.deleteFavorites(username);
        repository.delete(entity);
    }

    private UserEntity findUserByUsernameAndPassword(String username, String password) {
        UserEntity entity = findUserByUsername(username);
        if (!passwordEncoder.matches(password, entity.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return entity;
    }

    private UserEntity findUserByUsername(String username) {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(format("User with username '%s' is not found", username)));
    }
}
