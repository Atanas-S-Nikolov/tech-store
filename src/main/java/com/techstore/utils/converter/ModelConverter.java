package com.techstore.utils.converter;

import com.techstore.model.Product;
import com.techstore.model.User;
import com.techstore.model.dto.ProductDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.UserResponse;

public class ModelConverter {
    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());
    }

    public static Product toModel(ProductDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getStocks(), dto.getCategory(), dto.getType(),
                dto.isEarlyAccess(), dto.getDateOfModification(), null);
    }

    public static Product toModel(ProductEntity entity) {
        return new Product(entity.getName(), entity.getPrice(), entity.getStocks(), entity.getCategory(), entity.getType(),
                entity.isEarlyAccess(), entity.getDateOfModification(), entity.getImageUrls());
    }

    public static User toModel(UserDto dto) {
        return new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getUsername(), dto.getPassword(), null, null);
    }

    public static User toModel(UserEntity entity) {
        return new User(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getUsername(), entity.getPassword(), "", entity.getRole());
    }

    public static ProductEntity toEntity(Product product) {
        return new ProductEntity(null, product.getName(), product.getPrice(), product.getStocks(), product.getCategory(),
                product.getType(), product.isEarlyAccess(), product.getDateOfModification(), product.getImageUrls());
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(null, user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername(), user.getPassword(), null);
    }
}
