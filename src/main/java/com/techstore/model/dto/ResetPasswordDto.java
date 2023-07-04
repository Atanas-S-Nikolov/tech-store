package com.techstore.model.dto;

import com.techstore.validation.user.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ResetPasswordDto {
    @ValidPassword
    private final String password;

    @NotBlank(message = "Reset token must not be blank")
    private final String token;
}
