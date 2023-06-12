package com.techstore.repository;

import com.techstore.model.entity.RegisterConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRegisterConfirmationTokenRepository extends JpaRepository<RegisterConfirmationTokenEntity, String> {
    Optional<RegisterConfirmationTokenEntity> findByToken(String token);
}
