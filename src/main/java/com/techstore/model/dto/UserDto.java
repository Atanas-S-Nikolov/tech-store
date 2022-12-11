package com.techstore.model.dto;

import com.techstore.validation.user.ValidEmail;
import com.techstore.validation.user.ValidUser;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
@ValidUser
public class UserDto {
    @NotBlank(message = "First name must not be blank")
    private final String firstName;
    @NotBlank(message = "Last name must not be blank")
    private final String lastName;
    @ValidEmail
    private final String email;
    @NotBlank(message = "Username must not be blank")
    private final String username;
    private final String password;
    private final String newPassword;

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
}
