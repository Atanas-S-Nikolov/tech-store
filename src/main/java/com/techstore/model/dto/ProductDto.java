package com.techstore.model.dto;

import com.techstore.validation.product.ProductCategoryConstraint;
import com.techstore.validation.product.ValidProduct;
import com.techstore.validation.product.ProductTypeConstraint;
import com.techstore.validation.product.ValidProductName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
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
}
