package com.techstore.model;

import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final int stocks;
    private final ProductCategory category;
    private final ProductType type;
    private final boolean earlyAccess;
    private final LocalDateTime dateOfCreation;
    private final LocalDateTime dateOfModification;
    private final Set<String> imageUrls;

    public Product() {
        this("", new BigDecimal("0.0"), 0, null, null, false, null, null, new HashSet<>());
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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStocks() {
        return stocks;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public ProductType getType() {
        return type;
    }

    public boolean isEarlyAccess() {
        return earlyAccess;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public LocalDateTime getDateOfModification() {
        return dateOfModification;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stocks=" + stocks +
                ", category=" + category +
                ", type=" + type +
                ", earlyAccess=" + earlyAccess +
                ", dateOfCreation=" + dateOfCreation +
                ", dateOfModification=" + dateOfModification +
                ", imageUrls=" + imageUrls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return stocks == product.stocks && earlyAccess == product.earlyAccess && Objects.equals(name, product.name) && Objects.equals(price, product.price) && category == product.category && type == product.type && Objects.equals(dateOfCreation, product.dateOfCreation) && Objects.equals(dateOfModification, product.dateOfModification) && Objects.equals(imageUrls, product.imageUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, stocks, category, type, earlyAccess, dateOfCreation, dateOfModification, imageUrls);
    }
}
