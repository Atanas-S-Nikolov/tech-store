package com.techstore.model.dto;

import com.techstore.validation.product.ValidProductName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class ProductToBuyDto {
    @ValidProductName
    @Getter
    private final String productName;

    @Min(value = 1, message = "Quantity must be greater than 0")
    @Getter
    private final int quantity;
}
