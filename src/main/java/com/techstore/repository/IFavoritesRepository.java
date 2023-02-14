package com.techstore.repository;

import com.techstore.model.entity.FavoritesEntity;
import com.techstore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFavoritesRepository extends JpaRepository<FavoritesEntity, String> {

    @Query("SELECT f FROM FavoritesEntity f where f.user = :user")
    Optional<FavoritesEntity> findByUser(@Param(value = "user") UserEntity user);
}
