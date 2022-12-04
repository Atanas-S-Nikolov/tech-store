package com.techstore.model.dto;

import java.util.Objects;

public class AuthenticationDto {
    private final String username;
    private final String password;

    public AuthenticationDto() {
        this(null, null);
    }

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

    @Override
    public String toString() {
        return "AuthenticationDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationDto)) return false;
        AuthenticationDto authenticationDto = (AuthenticationDto) o;
        return Objects.equals(username, authenticationDto.username) && Objects.equals(password, authenticationDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
