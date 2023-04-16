package com.techstore.model.enums;

import com.techstore.constants.RoleConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@AllArgsConstructor
@ToString(doNotUseGetters = true)
public enum UserRole {
    ROLE_ADMIN(RoleConstants.ROLE_ADMIN),
    ROLE_CUSTOMER(RoleConstants.ROLE_CUSTOMER);

    @Getter
    private final String value;

    public static UserRole getKeyByValue(String value) {
        return Arrays.stream(values())
                .filter(userRole -> userRole.getValue().equals(value))
                .findFirst().orElse(null);
    }
}
