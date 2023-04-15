package com.techstore.model.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class OrderResponse {
    @Getter
    private final Set<PurchasedProductResponse> products;

    @Getter
    private final BigDecimal totalPrice;

    @Getter
    private final LocalDateTime date;
}
