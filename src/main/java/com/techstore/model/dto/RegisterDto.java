package com.techstore.model.dto;

import com.techstore.validation.user.ValidEmail;
import com.techstore.validation.user.ValidPhone;
import com.techstore.validation.user.ValidPassword;
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
public class RegisterDto{
    @NotBlank(message = "First name must not be blank")
    @Getter
    private final String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Getter
    private final String lastName;

    @ValidEmail
    @Getter
    private final String email;

    @ValidPhone
    @Getter
    private final String phone;

    @NotBlank(message = "Address must not be blank")
    @Getter
    private final String address;

    @NotBlank(message = "Username must not be blank")
    @Getter
    private final String username;

    @ValidPassword
    @Getter
    private final String password;

    @Getter
    @Setter
    private String role;
}
