package com.techstore.model.enums;

public enum ProductType {
    DesktopPC("Desktop PC"),
    Laptop("Laptop"),
    TV("TV"),
    Monitor("Monitor"),
    Keyboard("Keyboard"),
    Mouse("Mouse"),
    Headset("Headset"),
    Earphones("Earphones"),
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
