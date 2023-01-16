package com.techstore.model.dto;

import com.techstore.validation.user.ValidEmail;
import com.techstore.validation.user.ValidUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(force = true)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
@ValidUser
public class UserDto {
    @NotBlank(message = "First name must not be blank")
    @Getter
    private final String firstName;

    @Getter
    @NotBlank(message = "Last name must not be blank")
    private final String lastName;

    @ValidEmail
    @Getter
    private final String email;

    @Getter
    private final String phone;

    @NotBlank(message = "Username must not be blank")
    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final String newPassword;

    public UserDto(String firstName, String lastName, String email, String phone, String username, String password, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }
}
