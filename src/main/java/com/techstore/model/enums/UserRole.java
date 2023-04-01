package com.techstore.model.enums;

import com.techstore.constants.RoleConstants;

import java.util.Arrays;

public enum UserRole {
    ROLE_ADMIN(RoleConstants.ROLE_ADMIN),
    ROLE_CUSTOMER(RoleConstants.ROLE_CUSTOMER);

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRole getKeyByValue(String value) {
        return Arrays.stream(values())
                .filter(userRole -> userRole.getValue().equals(value))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "value='" + value + '\'' +
                '}';
    }
}
