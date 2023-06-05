package com.techstore.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString(doNotUseGetters = true)
public enum OrderStatus {
    CREATED("Created"),
    CANCELED("Canceled"),
    DELIVERED("Delivered"),
    RECEIVED("Received"),
    RETURNED("Returned");

    @Getter
    private final String value;
}
