package com.techstore.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
public class PurchasedProductResponse {
    @Getter
    private final String productName;

    @Getter
    private final long quantity;
}
