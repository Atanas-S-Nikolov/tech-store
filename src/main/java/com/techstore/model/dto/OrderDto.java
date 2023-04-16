package com.techstore.model.dto;

import com.techstore.validation.cart.ValidCartKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class OrderDto {
    @NotBlank(message = "Username must not be blank")
    @Getter
    private final String username;

    @ValidCartKey
    @Getter
    private final String cartKey;
}
