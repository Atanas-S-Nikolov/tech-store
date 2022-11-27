package com.techstore.model.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER");

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
