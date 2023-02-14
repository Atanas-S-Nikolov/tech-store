package com.techstore.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class AuthenticationDto {
    @NotBlank(message = "Username must not be blank")
    @Getter
    private final String username;

    @NotBlank(message = "Password must not be blank")
    @Getter
    private final String password;
}
