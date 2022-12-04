package com.techstore.model.enums;

import com.techstore.constants.RoleConstants;

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

    @Override
    public String toString() {
        return "UserRole{" +
                "value='" + value + '\'' +
                '}';
    }
}
