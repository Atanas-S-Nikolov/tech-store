package com.techstore.model;

import com.techstore.model.enums.UserRole;

import java.util.Objects;

public class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String username;
    private final String password;
    private final String newPassword;
    private final UserRole role;

    public User() {
        this(null, null, null, null, null, null, null);
    }

    public User(String firstName, String lastName, String email, String username, String password, String newPassword, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
        this.role = role;
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

    public UserRole getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(newPassword, user.newPassword) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, username, password, newPassword, role);
    }
}
