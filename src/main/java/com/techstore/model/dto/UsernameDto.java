package com.techstore.model.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class UsernameDto {
    @NotBlank(message = "Username must not be blank")
    private final String username;

    public UsernameDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
