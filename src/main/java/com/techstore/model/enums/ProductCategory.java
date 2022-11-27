package com.techstore.model.enums;

public enum ProductCategory {
    AUDIO("Audio"),
    COMPUTERS_AND_LAPTOPS("Computers and laptops"),
    MICE_AND_KEYBOARDS("Mice and keyboards"),
    TV_AND_MONITORS("TV and Monitors");

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