package com.techstore.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class ProductToBuyResponse {
    @Getter
    private final ProductResponse product;

    @Getter
    private final long quantity;

    public ProductToBuyResponse() {
        this(null, 0L);
    }

    public ProductToBuyResponse(ProductResponse product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
