package com.techstore.repository;

import com.techstore.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, String> {

    Optional<ProductEntity> findProductByName(String name);

    @Query("SELECT p FROM ProductEntity p where p.earlyAccess = :earlyAccess")
    List<ProductEntity> findProductsWithEarlyAccess(@Param("earlyAccess") boolean earlyAccess);
}
