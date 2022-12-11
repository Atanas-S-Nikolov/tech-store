package com.techstore.model.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class CartDto {
    @NotBlank(message = "Username must not be blank")
    private final String username;
    private final Set<ProductToBuyDto> productsToBuy;

    public CartDto(String username, Set<ProductToBuyDto> productsToBuy) {
        this.username = username;
        this.productsToBuy = productsToBuy;
    }

    public String getUsername() {
        return username;
    }

    public Set<ProductToBuyDto> getProductsToBuy() {
        return productsToBuy;
    }
}
