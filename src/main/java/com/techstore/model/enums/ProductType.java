package com.techstore.model.enums;

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

    @Override
    public String toString() {
        return "ProductType{" +
                "value='" + value + '\'' +
                '}';
    }
}
