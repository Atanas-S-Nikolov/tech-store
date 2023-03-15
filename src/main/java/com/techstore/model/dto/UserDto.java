package com.techstore.model.dto;

import com.techstore.model.enums.UserRole;
import com.techstore.validation.user.ValidEmail;
import com.techstore.validation.user.ValidUser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
@ValidUser
public class UserDto {
    @NotBlank(message = "First name must not be blank")
    @Getter
    private final String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Getter
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

    @Getter
    @Setter
    private UserRole role;
}
