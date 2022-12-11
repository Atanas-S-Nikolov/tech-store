package com.techstore.model;

import lombok.*;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class ProductToBuy {
    @Getter
    private final Product product;

    @Getter
    private final long quantity;
}
