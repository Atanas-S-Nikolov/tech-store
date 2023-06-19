package com.techstore.service.user;

import com.techstore.exception.user.UserNotFoundException;
import com.techstore.model.dto.EmailDto;
import com.techstore.model.dto.UpdateUserDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.entity.PasswordResetTokenEntity;
import com.techstore.model.entity.RegisterConfirmationTokenEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.GenericResponse;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.UserResponse;
import com.techstore.repository.IPasswordResetTokenRepository;
import com.techstore.repository.IRegisterConfirmationTokenRepository;
import com.techstore.repository.IUserRepository;
import com.techstore.service.favorites.IFavoritesService;
import com.techstore.service.mail.IMailSenderService;
import com.techstore.service.order.IOrderService;
import com.techstore.utils.converter.ModelConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.List;

import static com.techstore.constants.DateTimeConstants.FIVE_MINUTES;
import static com.techstore.constants.RoleConstants.ROLE_ADMIN;
import static com.techstore.utils.HashingUtils.hashSha256;
import static com.techstore.utils.auth.AuthUtils.checkOwner;
import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserService implements IUserService {
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final IFavoritesService favoritesService;
    private final IOrderService orderService;
    private final IRegisterConfirmationTokenRepository registerConfirmationTokenRepository;
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final IMailSenderService mailSenderService;

    public UserService(IUserRepository repository, PasswordEncoder passwordEncoder, IFavoritesService favoritesService,
                       IOrderService orderService, IRegisterConfirmationTokenRepository registerConfirmationTokenRepository,
                       IPasswordResetTokenRepository passwordResetTokenRepository, IMailSenderService mailSenderService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.favoritesService = favoritesService;
        this.orderService = orderService;
        this.registerConfirmationTokenRepository = registerConfirmationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailSenderService = mailSenderService;
    }

    @Transactional
    @Override
    public UserResponse createUser(UserDto user) {
        UserEntity userEntity = toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword().trim()));

        if (user.getRole().equals(ROLE_ADMIN)) {
            userEntity.setEnabled(true);
            return toResponse(repository.save(userEntity));
        }

        String userEmail = userEntity.getEmail();
        long currentTimeMs = currentTimeMillis();
        long randomLong = currentTimeMs + userEntity.getUsername().hashCode() + userEmail.hashCode();
        long tokenExpirationMs = currentTimeMs + FIVE_MINUTES;
        String tokenValue = hashSha256(Long.toString(randomLong));
        RegisterConfirmationTokenEntity registerConfirmationToken =
                new RegisterConfirmationTokenEntity(null, tokenValue, tokenExpirationMs, userEntity);
        UserEntity savedUser = repository.save(userEntity);
        favoritesService.createDefaultFavorites(savedUser);
        registerConfirmationToken.setUser(userEntity);
        registerConfirmationTokenRepository.save(registerConfirmationToken);
        mailSenderService.sendRegistrationConfirmationMail(userEmail, tokenValue, tokenExpirationMs);
        return toResponse(savedUser);
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
                existingEntity.isEnabled(), existingEntity.getRegisterConfirmationToken(), existingEntity.getPasswordResetToken(),
                existingEntity.getFavorite(), existingEntity.getOrders());

        String newPassword = user.getNewPassword();
        if (nonNull(newPassword) && !newPassword.trim().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(newPassword));
        }

        return toResponse(repository.save(entity));
    }

    @Transactional
    @Override
    public GenericResponse forgotPassword(EmailDto emailDto) {
        String userEmail = emailDto.getEmail();
        UserEntity userEntity = repository.findUserByEmail(userEmail);

        if (userEntity != null) {
            long currentTimeMs = currentTimeMillis();
            long randomLong = currentTimeMs + userEntity.getPassword().hashCode() + userEmail.hashCode();
            long tokenExpirationMs = currentTimeMs + FIVE_MINUTES;
            String tokenValue = hashSha256(Long.toString(randomLong));
            PasswordResetTokenEntity passwordResetTokenEntity =
                    new PasswordResetTokenEntity(null, tokenValue, tokenExpirationMs, userEntity);
            passwordResetTokenRepository.save(passwordResetTokenEntity);
            mailSenderService.sendForgottenPasswordMail(userEmail, tokenValue, tokenExpirationMs);
        }

        return new GenericResponse("Email sent");
    }

    @Transactional
    @Override
    public void deleteUser(String username) {
        UserEntity entity = findUserByUsername(username);
        favoritesService.deleteFavorites(username);
        orderService.deleteOrdersForUser(username);
        RegisterConfirmationTokenEntity registerConfirmationToken = entity.getRegisterConfirmationToken();
        if (nonNull(registerConfirmationToken)) {
            registerConfirmationTokenRepository.delete(registerConfirmationToken);
        }
        repository.delete(entity);
    }

    private UserEntity findUserByUsername(String username) {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(format("User with username '%s' is not found", username)));
    }
}
