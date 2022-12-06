package com.techstore.model.entity;

import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.nonNull;

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
    private double price;

    @Column(name = "stocks")
    private int stocks;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;

    @Column(name = "early_access")
    private boolean earlyAccess;

    @Column(name = "date_of_modification")
    private Date dateOfModification;

    @ElementCollection
    @Column(name = "image_urls", length = 10000)
    private Set<String> imageUrls;

    public ProductEntity() {
        this(null ,null, 0.0, 0, null, null, false, null, new HashSet<>());
    }

    public ProductEntity(String id, String name, double price, int stocks, ProductCategory category, ProductType type, boolean earlyAccess, Date dateOfModification, Set<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stocks = stocks;
        this.category = category;
        this.type = type;
        this.earlyAccess = earlyAccess;
        this.dateOfModification = dateOfModification;
        this.imageUrls = nonNull(imageUrls) ? imageUrls : new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public boolean isEarlyAccess() {
        return earlyAccess;
    }

    public void setEarlyAccess(boolean earlyAccess) {
        this.earlyAccess = earlyAccess;
    }

    public Date getDateOfModification() {
        return dateOfModification;
    }

    public void setDateOfModification(Date dateOfModification) {
        this.dateOfModification = dateOfModification;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stocks=" + stocks +
                ", category=" + category +
                ", type=" + type +
                ", earlyAccess=" + earlyAccess +
                ", dateOfModification=" + dateOfModification +
                ", imageUrls=" + imageUrls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEntity)) return false;
        ProductEntity that = (ProductEntity) o;
        return Double.compare(that.price, price) == 0 && stocks == that.stocks && earlyAccess == that.earlyAccess && Objects.equals(id, that.id) && Objects.equals(name, that.name) && category == that.category && type == that.type && Objects.equals(dateOfModification, that.dateOfModification) && Objects.equals(imageUrls, that.imageUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stocks, category, type, earlyAccess, dateOfModification, imageUrls);
    }
}
