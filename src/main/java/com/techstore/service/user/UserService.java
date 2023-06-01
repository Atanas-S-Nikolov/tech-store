package com.techstore.service.user;

import com.techstore.exception.user.UserNotFoundException;
import com.techstore.model.dto.UpdateUserDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.UserResponse;
import com.techstore.repository.IUserRepository;
import com.techstore.service.favorites.IFavoritesService;
import com.techstore.service.order.IOrderService;
import com.techstore.utils.converter.ModelConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.List;

import static com.techstore.utils.auth.AuthUtils.checkOwner;
import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserService implements IUserService {
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final IFavoritesService favoritesService;
    private final IOrderService orderService;

    public UserService(IUserRepository repository, PasswordEncoder passwordEncoder, IFavoritesService favoritesService,
                       IOrderService orderService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.favoritesService = favoritesService;
        this.orderService = orderService;
    }

    @Transactional
    @Override
    public UserResponse createUser(UserDto user) {
        UserEntity entity = toEntity(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        entity.setFavorite(favoritesService.createDefaultFavorites());
        return toResponse(repository.save(entity));
    }

    @Override
    public UserResponse getUser(String username) {
        checkOwner(username);
        return toResponse(findUserByUsername(username));
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(Integer page, Integer size) {
        Page<UserEntity> usersPage = repository.findAll(PageRequest.of(page, size));
        List<UserResponse> users = usersPage.stream().map(ModelConverter::toResponse).collect(toList());
        return new PageResponse<>(usersPage.getTotalElements(), usersPage.getTotalPages(), usersPage.getNumber(), users);
    }

    @Override
    public UserResponse updateUser(UpdateUserDto user) {
        UserEntity existingEntity = findUserByUsername(user.getUsername());
        UserEntity entity =  new UserEntity(existingEntity.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPhone(), user.getAddress(), existingEntity.getUsername(), existingEntity.getPassword(), existingEntity.getRole(),
                existingEntity.getFavorite(), existingEntity.getOrders());

        String newPassword = user.getNewPassword();
        if (nonNull(newPassword) && !newPassword.trim().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(newPassword));
        }

        return toResponse(repository.save(entity));
    }

    @Transactional
    @Override
    public void deleteUser(String username) {
        UserEntity entity = findUserByUsername(username);
        favoritesService.deleteFavorites(username);
        orderService.deleteOrdersForUser(username);
        repository.delete(entity);
    }

    private UserEntity findUserByUsername(String username) {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(format("User with username '%s' is not found", username)));
    }
}
