package com.techstore.repository;

import com.techstore.model.entity.ProductEntity;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<ProductEntity, String> {
    Optional<ProductEntity> findProductByName(String name);

    Page<ProductEntity> findAllByCategory(ProductCategory category, Pageable pageable);

    Page<ProductEntity> findAllByCategoryAndType(ProductCategory category, ProductType type, Pageable pageable);

    Page<ProductEntity> findAllByEarlyAccess(boolean earlyAccess, Pageable pageable);

    Page<ProductEntity> findAllByEarlyAccessAndCategory(boolean earlyAccess, ProductCategory category, Pageable pageable);

    Page<ProductEntity> findAllByEarlyAccessAndCategoryAndType(boolean earlyAccess, ProductCategory category, ProductType type,
                                                                       Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM Products p WHERE CONCAT(p.name, ' ', p.category, ' ', p.type) LIKE %?1% LIMIT 5"
    )
    List<ProductEntity> search(String keyword);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM Products p WHERE CONCAT(p.name, ' ', p.category, ' ', p.type) LIKE %?1% " +
                    "AND p.early_access = FALSE LIMIT 5"
    )
    List<ProductEntity> searchWithoutEarlyAccess(String keyword);

    @Query("SELECT p FROM ProductEntity p WHERE CONCAT(p.name, ' ', p.category, ' ', p.type) LIKE %?1%")
    Page<ProductEntity> searchQuery(String keyword, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE CONCAT(p.name, ' ', p.category, ' ', p.type) LIKE %?1% AND p.earlyAccess = false")
    Page<ProductEntity> searchQueryWithoutEarlyAccess(String keyword, Pageable pageable);
}
