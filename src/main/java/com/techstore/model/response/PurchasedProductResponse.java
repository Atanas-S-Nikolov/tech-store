package com.techstore.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
public class PurchasedProductResponse {
    @Getter
    private final String productName;

    @Getter
    private final BigDecimal price;

    @Getter
    private final long quantity;
}
