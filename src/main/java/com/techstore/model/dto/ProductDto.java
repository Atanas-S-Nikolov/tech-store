package com.techstore.model.dto;

import com.techstore.validation.product.ProductCategoryConstraint;
import com.techstore.validation.product.ValidProduct;
import com.techstore.validation.product.ProductTypeConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
@ValidProduct
public class ProductDto {
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    @Getter
    private final BigDecimal price;

    @Min(value = 0, message = "Product stocks must be equal or greater than 0")
    @Max(value = 1000, message = "Product stocks must be less than 1000")
    @Getter
    private final int stocks;

    @ProductCategoryConstraint
    @Getter
    private final String category;

    @ProductTypeConstraint
    @Getter
    private final String type;

    @NotBlank(message = "Product brand must not be blank")
    @Getter
    private final String brand;

    @NotBlank(message = "Product model must not be blank")
    @Getter
    private final String model;

    @Getter
    private final String description;

    @Getter
    private final boolean earlyAccess;
}
