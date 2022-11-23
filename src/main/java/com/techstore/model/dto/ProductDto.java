package com.techstore.model.dto;

import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;

import java.util.Date;
import java.util.Objects;

public class ProductDto {
    private final String name;
    private final double price;
    private final int stocks;
    private final ProductCategory category;
    private final ProductType type;
    private final Date dateOfCreation;

    public ProductDto() {
        this("", 0.0, 0, null, null);
    }

    public ProductDto(String name, double price, int stocks, ProductCategory category, ProductType type) {
        this.name = name;
        this.price = price;
        this.stocks = stocks;
        this.category = category;
        this.type = type;
        this.dateOfCreation = new Date();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
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

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stocks=" + stocks +
                ", category=" + category +
                ", type=" + type +
                ", dateOfCreation=" + dateOfCreation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDto)) return false;
        ProductDto that = (ProductDto) o;
        return Double.compare(that.price, price) == 0 && stocks == that.stocks && Objects.equals(name, that.name) && category == that.category && type == that.type && Objects.equals(dateOfCreation, that.dateOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, stocks, category, type, dateOfCreation);
    }
}
