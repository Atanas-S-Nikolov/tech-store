package com.techstore.model.dto;

import com.techstore.validation.product.ProductCategoryConstraint;
import com.techstore.validation.product.ValidProduct;
import com.techstore.validation.product.ProductTypeConstraint;
import com.techstore.validation.product.ValidProductName;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Objects;

@ValidProduct
public class ProductDto {
    @ValidProductName
    private final String name;
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    private final BigDecimal price;
    @Min(value = 0, message = "Product stocks must be equal or greater than 0")
    private final int stocks;
    @ProductCategoryConstraint
    private final String category;
    @ProductTypeConstraint
    private final String type;
    private final boolean earlyAccess;

    public ProductDto() {
        this("", new BigDecimal("0.0"), 0, "", "", false);
    }

    public ProductDto(String name, BigDecimal price, int stocks, String category, String type, boolean earlyAccess) {
        this.name = name;
        this.price = price;
        this.stocks = stocks;
        this.category = category;
        this.type = type;
        this.earlyAccess = earlyAccess;
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

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public boolean isEarlyAccess() {
        return earlyAccess;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stocks=" + stocks +
                ", category=" + category +
                ", type=" + type +
                ", earlyAccess=" + earlyAccess +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDto)) return false;
        ProductDto that = (ProductDto) o;
        return stocks == that.stocks && earlyAccess == that.earlyAccess && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(category, that.category) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, stocks, category, type, earlyAccess);
    }
}
