package com.techstore.model.dto;

import com.techstore.validation.cart.ValidCartKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.Set;

@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class UpdateCartDto extends CartDto {
    @ValidCartKey
    @Getter
    private final String cartKey;

    public UpdateCartDto() {
        super();
        this.cartKey = null;
    }

    public UpdateCartDto(@Valid Set<ProductToBuyDto> productsToBuy, String cartKey) {
        super(productsToBuy);
        this.cartKey = cartKey;
    }
}
