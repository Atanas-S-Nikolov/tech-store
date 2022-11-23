package com.techstore.model;

import com.techstore.model.dto.ProductDto;
import com.techstore.model.entity.ProductEntity;

public class ModelConverter {
    public static Product toModel(ProductDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getStocks(), dto.getCategory(), dto.getType(),
                dto.getDateOfCreation(), null);
    }

    public static Product toModel(ProductEntity entity) {
        return new Product(entity.getName(), entity.getPrice(), entity.getStocks(), entity.getCategory(), entity.getType(),
                entity.getDateOfCreation(), entity.getImageUrls());
    }

    public static ProductEntity toEntity(Product product) {
        return new ProductEntity(null, product.getName(), product.getPrice(), product.getStocks(), product.getCategory(),
                product.getType(), product.getDateOfCreation(), product.getImageUrls());
    }
}
