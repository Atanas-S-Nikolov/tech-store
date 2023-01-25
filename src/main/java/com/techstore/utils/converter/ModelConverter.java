package com.techstore.utils.converter;

import com.techstore.model.Cart;
import com.techstore.model.Product;
import com.techstore.model.User;
import com.techstore.model.dto.ProductDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.ProductToBuyEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import com.techstore.model.response.ProductToBuyResponse;
import com.techstore.model.response.UserResponse;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ModelConverter {
    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getUsername());
    }

    public static ProductToBuyResponse toResponse(ProductToBuyEntity toBuyEntity) {
        return new ProductToBuyResponse(toModel(toBuyEntity), toBuyEntity.getQuantity());
    }

    public static Product toModel(ProductDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getStocks(), ProductCategory.getKeyByValue(dto.getCategory()),
                ProductType.getKeyByValue(dto.getType()), dto.getBrand(), dto.getModel(), dto.getDescription(), dto.isEarlyAccess(),
                null, null, null);
    }

    public static Product toModel(ProductEntity entity) {
        return new Product(entity.getName(), entity.getPrice(), entity.getStocks(), entity.getCategory(), entity.getType(),
                entity.getBrand(), entity.getModel(), entity.getDescription(), entity.isEarlyAccess(), entity.getDateOfCreation(),
                entity.getDateOfModification(), entity.getImageUrls());
    }

    public static Product toModel(ProductToBuyEntity toBuyEntity) {
        return toModel(toBuyEntity.getProduct());
    }

    public static User toModel(UserDto dto) {
        return new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getPhone(), dto.getUsername(), dto.getPassword(),
                null, null);
    }

    public static User toModel(UserEntity entity) {
        return new User(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPhone(), entity.getUsername(),
                entity.getPassword(), "", entity.getRole());
    }

    public static Cart toModel(CartEntity entity) {
        UserResponse userResponse = toResponse(toModel(entity.getUser()));
        return new Cart(userResponse, convertEntitiesToResponses(entity.getProductsToBuy()), entity.getTotalPrice());
    }

    public static ProductEntity toEntity(Product product) {
        return new ProductEntity(null, product.getName(), product.getPrice(), product.getStocks(), product.getCategory(),
                product.getType(), product.getBrand(), product.getModel(), product.getDescription(), product.isEarlyAccess(),
                product.getDateOfCreation(), product.getDateOfModification(), product.getImageUrls(), null);
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(null, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getUsername(),
                user.getPassword(), null, null);
    }

    public static Set<ProductToBuyResponse> convertEntitiesToResponses(Set<ProductToBuyEntity> entities) {
        return entities.stream().map(ModelConverter::toResponse).collect(toSet());
    }
}
