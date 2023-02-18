package com.techstore.repository;

import com.techstore.model.entity.ProductToBuyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductToBuyRepository extends JpaRepository<ProductToBuyEntity, String> {

    @Query("SELECT pb FROM ProductToBuyEntity pb where pb.product.name = :productName")
    Optional<ProductToBuyEntity> findProductToBuyByName(@Param("productName") String productName);
}
