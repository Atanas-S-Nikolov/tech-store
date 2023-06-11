package com.techstore.repository;

import com.techstore.model.entity.PasswordResetTokenEntity;
import com.techstore.model.entity.RegisterConfirmationTokenEntity;
import com.techstore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<UserEntity, String> {
    Optional<UserEntity> findUserByUsername(String username);

    UserEntity findUserByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.registerConfirmationToken = :token")
    Optional<UserEntity> findUserByRegisterConfirmationToken(@Param(value = "token") RegisterConfirmationTokenEntity token);

    @Query("SELECT u FROM UserEntity u WHERE u.passwordResetToken = :token")
    Optional<UserEntity> findUserByPasswordResetToken(@Param(value = "token") PasswordResetTokenEntity token);
}
