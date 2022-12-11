package com.techstore.model.enums;

import java.util.Arrays;

public enum ProductType {
    DESKTOP_PC("Desktop PC"),
    LAPTOP("Laptop"),
    TV("TV"),
    MONITOR("Monitor"),
    KEYBOARD("Keyboard"),
    MOUSE("Mouse"),
    HEADSET("Headset"),
    EARPHONES("Earphones"),
    TWS("TWS");

    private final String value;

    ProductType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ProductType getKeyByValue(String value) {
        return Arrays.stream(values())
                .filter(productType -> productType.getValue().equals(value))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "value='" + value + '\'' +
                '}';
    }
}
