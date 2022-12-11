package com.techstore.model.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class UsernameDto {
    private final String username;

    public UsernameDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
