package com.techstore.model.entity;

import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.nonNull;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stocks")
    private int stocks;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "early_access")
    private boolean earlyAccess;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "date_of_modification")
    private LocalDateTime dateOfModification;

    @ElementCollection
    @Column(name = "image_urls", length = 10000)
    private Set<String> imageUrls;

    @OneToOne(mappedBy = "product")
    private ProductToBuyEntity productToBuy;

    public ProductEntity() {
        this(null ,null, new BigDecimal("0.0"), 0, null, null, null, null, null,false, null, null, new HashSet<>(), null);
    }

    public ProductEntity(String id, String name, BigDecimal price, int stocks, ProductCategory category, ProductType type,
                         String brand, String model, String description, boolean earlyAccess, LocalDateTime dateOfCreation, LocalDateTime dateOfModification,
                         Set<String> imageUrls, ProductToBuyEntity productToBuy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stocks = stocks;
        this.category = category;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.earlyAccess = earlyAccess;
        this.dateOfCreation = dateOfCreation;
        this.dateOfModification = dateOfModification;
        this.imageUrls = nonNull(imageUrls) ? imageUrls : new HashSet<>();
        this.productToBuy = productToBuy;
    }
}
