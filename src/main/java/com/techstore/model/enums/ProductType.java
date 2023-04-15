package com.techstore.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@AllArgsConstructor
@ToString(doNotUseGetters = true)
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

    @Getter
    private final String value;

    public static ProductType getKeyByValue(String value) {
        return Arrays.stream(values())
                .filter(productType -> productType.getValue().equals(value))
                .findFirst().orElse(null);
    }
}
