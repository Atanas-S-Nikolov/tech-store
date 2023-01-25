package com.techstore.model;

import com.techstore.model.response.ProductToBuyResponse;
import com.techstore.model.response.UserResponse;
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
public class Cart {
    @Getter
    private final UserResponse user;

    @Getter
    private final Set<ProductToBuyResponse> products;

    @Getter
    private final BigDecimal totalPrice;
}

