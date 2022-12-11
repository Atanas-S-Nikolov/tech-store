package com.techstore.utils.converter;

import com.techstore.model.Cart;
import com.techstore.model.Product;
import com.techstore.model.ProductToBuy;
import com.techstore.model.User;
import com.techstore.model.dto.CartDto;
import com.techstore.model.dto.ProductDto;
import com.techstore.model.dto.ProductToBuyDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import com.techstore.model.response.UserResponse;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ModelConverter {
    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());
    }

    public static Product toModel(ProductDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getStocks(), ProductCategory.getKeyByValue(dto.getCategory()),
                ProductType.getKeyByValue(dto.getType()), dto.isEarlyAccess(), null, null, null);
    }

    public static Product toModel(ProductEntity entity) {
        return new Product(entity.getName(), entity.getPrice(), entity.getStocks(), entity.getCategory(), entity.getType(),
                entity.isEarlyAccess(), entity.getDateOfCreation(), entity.getDateOfModification(), entity.getImageUrls());
    }

    public static User toModel(UserDto dto) {
        return new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getUsername(), dto.getPassword(),
                null, null);
    }

    public static User toModel(UserEntity entity) {
        return new User(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getUsername(), entity.getPassword(),
                "", entity.getRole());
    }

    public static Cart toModel(CartDto dto) {
        return new Cart(new UserResponse(dto.getUsername()), convertProductNamesToModels(dto.getProductsToBuy()),dto.getTotalPrice());
    }

    public static Cart toModel(CartEntity entity) {
        UserResponse userResponse = toResponse(toModel(entity.getUser()));
        return new Cart(userResponse, convertEntitiesToModels(entity.getProducts()), entity.getTotalPrice());
    }

    public static ProductEntity toEntity(Product product) {
        return new ProductEntity(null, product.getName(), product.getPrice(), product.getStocks(), product.getCategory(),
                product.getType(), product.isEarlyAccess(), product.getDateOfCreation(), product.getDateOfModification(),
                product.getImageUrls(), null);
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(null, user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername(),
                user.getPassword(), null, null);
    }

    public static Set<Product> convertEntitiesToModels(Set<ProductEntity> entities) {
        return entities.stream().map(ModelConverter::toModel).collect(toSet());
    }

    public static Set<Product> convertProductNamesToModels(Set<ProductToBuyDto> productsToBuy) {
        return productsToBuy.stream()
                .map(productToBuy -> new Product(productToBuy.getProductName()))
                .collect(toSet());
    }

    public static ProductToBuy convertProductToProductToBuy(ProductEntity entity, long quantity) {
        return new ProductToBuy(toModel(entity), quantity);
    }
}
