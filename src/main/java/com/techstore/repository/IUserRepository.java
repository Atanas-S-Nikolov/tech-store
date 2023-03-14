package com.techstore.repository;

import com.techstore.model.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<UserEntity, String> {

    Optional<UserEntity> findUserByUsername(String username);
}
