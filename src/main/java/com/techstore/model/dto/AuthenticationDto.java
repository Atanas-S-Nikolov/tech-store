package com.techstore.model.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class AuthenticationDto {
    @NotBlank(message = "Username must not be blank")
    private final String username;

    @NotBlank(message = "Password must not be blank")
    private final String password;

    public AuthenticationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
