package com.techstore.model.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class ProductToBuyDto {
    private final String productName;
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
