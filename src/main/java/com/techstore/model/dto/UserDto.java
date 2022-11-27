package com.techstore.model.dto;

import java.util.Objects;

public class UserDto {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String username;
    private final String password;
    private final String newPassword;

    public UserDto() {
        this(null, null, null, null, null, null);
    }

    public UserDto(String firstName, String lastName, String email, String username, String password, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(email, userDto.email) && Objects.equals(username, userDto.username) && Objects.equals(password, userDto.password) && Objects.equals(newPassword, userDto.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, username, password, newPassword);
    }
}
