package com.techstore.model;

import com.techstore.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class User {
    @Getter
    private final String firstName;

    @Getter
    private final String lastName;

    @Getter
    private final String email;

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final String newPassword;

    @Getter
    private final UserRole role;
}
