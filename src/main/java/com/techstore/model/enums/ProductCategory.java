package com.techstore.model.enums;

public enum ProductCategory {
    Audio("Audio"),
    ComputersAndLaptops("Computers and laptops"),
    MiceAndKeyboards("Mice and keyboards"),
    TVAndMonitors("TV and Monitors");

    private final String value;

    ProductCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "value='" + value + '\'' +
                '}';
    }
}