package com.techstore.repository;

import com.techstore.model.entity.PurchasedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPurchasedProductRepository extends JpaRepository<PurchasedProductEntity, String> {
}
