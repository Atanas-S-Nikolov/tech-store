package com.techstore.repository;

import com.techstore.model.entity.ProductEntity;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<ProductEntity, String> {
    Optional<ProductEntity> findProductByName(String name);

    Page<ProductEntity> findAllByCategory(ProductCategory category, Pageable pageable);

    Page<ProductEntity> findAllByCategoryAndType(ProductCategory category, ProductType type, Pageable pageable);

    Page<ProductEntity> findAllByEarlyAccess(boolean earlyAccess, Pageable pageable);

    Page<ProductEntity> findAllByEarlyAccessAndCategory(boolean earlyAccess, ProductCategory category, Pageable pageable);

    Page<ProductEntity> findAllByEarlyAccessAndCategoryAndType(boolean earlyAccess, ProductCategory category, ProductType type,
                                                                       Pageable pageable);
}
