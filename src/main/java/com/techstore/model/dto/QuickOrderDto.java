package com.techstore.model.dto;

import com.techstore.validation.order.ValidQuickOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ValidQuickOrder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class QuickOrderDto {
    @NotBlank(message = "First name must not be blank")
    private final String firstName;

    @NotBlank(message = "Last name must not be blank")
    private final String lastName;

    private final String email;

    private final String phone;

    @NotBlank(message = "Address must not be blank")
    private final String address;
}
