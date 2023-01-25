package com.techstore.repository;

import com.techstore.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findProductByName(String name);
}
