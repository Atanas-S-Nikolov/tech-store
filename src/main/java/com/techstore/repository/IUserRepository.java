package com.techstore.repository;

import com.techstore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findUserByUsername(String username);
}
