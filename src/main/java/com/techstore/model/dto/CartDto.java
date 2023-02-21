package com.techstore.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class CartDto {
    @NotBlank(message = "Username must not be blank")
    @Getter
    private final String username;

    @Valid
    @Getter
    private final Set<ProductToBuyDto> productsToBuy;
}
