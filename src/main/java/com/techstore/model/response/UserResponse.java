package com.techstore.model.response;

import java.util.Objects;

public class UserResponse {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String username;

    public UserResponse() {
        this(null);
    }

    public UserResponse(String username) {
        this(null, null, null, username);
    }

    public UserResponse(String firstName, String lastName, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
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

    @Override
    public String toString() {
        return "UserResponse{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponse)) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, username);
    }
}
