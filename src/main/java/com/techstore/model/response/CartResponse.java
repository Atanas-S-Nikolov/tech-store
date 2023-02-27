package com.techstore.model.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class CartResponse {
    @Getter
    private final UserResponse user;

    @Getter
    private final Set<ProductToBuyResponse> products;

    @Getter
    private final BigDecimal totalPrice;
}

