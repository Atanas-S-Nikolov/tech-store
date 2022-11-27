package com.techstore.model;

import com.techstore.model.dto.ProductDto;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.UserEntity;

public class ModelConverter {
    public static Product toModel(ProductDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getStocks(), dto.getCategory(), dto.getType(),
                dto.getDateOfCreation(), null);
    }

    public static Product toModel(ProductEntity entity) {
        return new Product(entity.getName(), entity.getPrice(), entity.getStocks(), entity.getCategory(), entity.getType(),
                entity.getDateOfCreation(), entity.getImageUrls());
    }

    public static User toModel(UserEntity entity) {
        return new User(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getUsername(), entity.getPassword(), "", entity.getRole());
    }

    public static ProductEntity toEntity(Product product) {
        return new ProductEntity(null, product.getName(), product.getPrice(), product.getStocks(), product.getCategory(),
                product.getType(), product.getDateOfCreation(), product.getImageUrls());
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(null, user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername(), user.getPassword(), null);
    }
}
