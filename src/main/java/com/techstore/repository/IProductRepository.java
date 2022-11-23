package com.techstore.repository;

import com.techstore.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<ProductEntity, String> {

    Optional<ProductEntity> findProductByName(String name);
}
