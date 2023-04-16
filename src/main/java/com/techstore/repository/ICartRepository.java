package com.techstore.repository;

import com.techstore.model.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<CartEntity, String> {
    @Query("SELECT c FROM CartEntity c where c.cartKey = :cartKey")
    Optional<CartEntity> findByKey(@Param(value = "cartKey") String cartKey);
}
