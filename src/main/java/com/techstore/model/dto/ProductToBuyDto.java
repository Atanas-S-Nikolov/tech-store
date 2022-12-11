package com.techstore.model.dto;

import com.techstore.validation.product.ValidProductName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class ProductToBuyDto {
    @ValidProductName
    private final String productName;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private final long quantity;

    public ProductToBuyDto(String productName, long quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public long getQuantity() {
        return quantity;
    }
}
