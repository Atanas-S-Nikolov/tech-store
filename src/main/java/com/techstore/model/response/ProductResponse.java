package com.techstore.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ProductResponse {
    private final String name;

    private final BigDecimal price;

    private final int stocks;

    private final String category;

    private final String type;

    private final String brand;

    private final String model;

    private final String description;

    private final boolean earlyAccess;

    private final LocalDateTime dateOfCreation;

    private final LocalDateTime dateOfModification;

    private final Collection<ImageResponse> images;
}
