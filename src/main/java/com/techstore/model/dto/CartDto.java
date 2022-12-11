package com.techstore.model.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class CartDto {
    private final String username;
    private final Set<ProductToBuyDto> productsToBuy;
    private final BigDecimal totalPrice;

    public CartDto(String username, Set<ProductToBuyDto> productsToBuy, BigDecimal totalPrice) {
        this.username = username;
        this.productsToBuy = productsToBuy;
        this.totalPrice = totalPrice;
    }

    public String getUsername() {
        return username;
    }

    public Set<ProductToBuyDto> getProductsToBuy() {
        return productsToBuy;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
