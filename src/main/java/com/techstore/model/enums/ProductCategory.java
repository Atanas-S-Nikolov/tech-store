package com.techstore.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@AllArgsConstructor
@ToString(doNotUseGetters = true)
public enum ProductCategory {
    AUDIO("Audio"),
    COMPUTERS_AND_LAPTOPS("Computers and laptops"),
    MICE_AND_KEYBOARDS("Mice and keyboards"),
    TV_AND_MONITORS("TV and Monitors");

    @Getter
    private final String value;

    public static ProductCategory getKeyByValue(String value) {
        return Arrays.stream(values())
                .filter(productType -> productType.getValue().equals(value))
                .findFirst().orElse(null);
    }
}