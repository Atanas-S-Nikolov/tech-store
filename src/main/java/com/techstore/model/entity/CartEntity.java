package com.techstore.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true)
    private String id;

    @OneToOne(mappedBy = "cart")
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "carts_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductEntity> products;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public CartEntity() {
        this("", null, new HashSet<>(), BigDecimal.ZERO);
    }

    public CartEntity(String id, UserEntity user, Set<ProductEntity> products, BigDecimal totalPrice) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.totalPrice = totalPrice;
    }
}
