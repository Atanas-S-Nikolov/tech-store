package com.techstore.model;

import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class Product {
    @Getter
    private final String name;

    @Getter
    private final BigDecimal price;

    @Getter
    private final int stocks;

    @Getter
    private final ProductCategory category;

    @Getter
    private final ProductType type;

    @Getter
    private final boolean earlyAccess;

    @Getter
    private final LocalDateTime dateOfCreation;

    @Getter
    private final LocalDateTime dateOfModification;

    @Getter
    private final Set<String> imageUrls;

    public Product() {
        this("");
    }

    public Product(String name) {
        this(name, new BigDecimal("0.0"), 0, null, null, false, null, null, new HashSet<>());
    }

    public Product(String name, BigDecimal price, int stocks, ProductCategory category, ProductType type, boolean earlyAccess, LocalDateTime dateOfCreation, LocalDateTime dateOfModification, Set<String> imageUrls) {
        this.name = name;
        this.price = price;
        this.stocks = stocks;
        this.category = category;
        this.type = type;
        this.earlyAccess = earlyAccess;
        this.dateOfCreation = dateOfCreation;
        this.dateOfModification = dateOfModification;
        this.imageUrls = imageUrls;
    }
}
