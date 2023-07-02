package com.techstore.repository;

import com.techstore.model.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface IOrderRepository extends PagingAndSortingRepository<OrderEntity, String> {
    @Query("SELECT o from OrderEntity o where o.user.username = :username AND o.cartKey = :cartKey")
    Optional<OrderEntity> findByUsernameAndCartKey(@Param("username") String username, @Param("cartKey") String cartKey);

    @Query("SELECT o from OrderEntity o where o.user.username = :username")
    Collection<OrderEntity> findAllByUsername(@Param("username") String username);

    @Query("SELECT o from OrderEntity o where o.date BETWEEN :startDate AND :endDate")
    Page<OrderEntity> findAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                          Pageable pageable);

    @Query("SELECT o from OrderEntity o where o.user.username LIKE  %:username% AND o.date BETWEEN :startDate AND :endDate")
    Page<OrderEntity> searchByUsernameBetweenDates(@Param("username") String username, @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
