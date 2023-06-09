package com.techstore.utils.converter;

import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.FavoritesEntity;
import com.techstore.model.entity.OrderEntity;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.ProductToBuyEntity;
import com.techstore.model.entity.PurchasedProductEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.enums.UserRole;
import com.techstore.model.dto.ProductDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import com.techstore.model.response.CartResponse;
import com.techstore.model.response.FavoritesResponse;
import com.techstore.model.response.OrderResponse;
import com.techstore.model.response.ProductResponse;
import com.techstore.model.response.ProductToBuyResponse;
import com.techstore.model.response.PurchasedProductResponse;
import com.techstore.model.response.UserResponse;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

public class ModelConverter {
    public static UserResponse toResponse(UserEntity entity) {
        return new UserResponse(entity.getUsername(), entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getPhone(), entity.getAddress(), entity.getRole());
    }

    public static ProductToBuyResponse toResponse(ProductToBuyEntity toBuyEntity) {
        return new ProductToBuyResponse(toProductResponse(toBuyEntity), toBuyEntity.getQuantity());
    }

    public static CartResponse toResponse(CartEntity entity) {
        return new CartResponse(entity.getCartKey(), convertEntitiesToResponses(entity.getProductsToBuy()), entity.getTotalPrice());
    }

    public static FavoritesResponse toResponse(FavoritesEntity entity) {
        Set<ProductResponse> productResponses = entity.getProducts().stream().map(ModelConverter::toResponse).collect(toSet());
        return new FavoritesResponse(toResponse(entity.getUser()), productResponses);
    }

    public static ProductResponse toResponse(ProductEntity entity) {
        return new ProductResponse(entity.getName(), entity.getPrice(), entity.getStocks(), entity.getCategory().getValue(),
                entity.getType().getValue(), entity.getBrand(), entity.getModel(), entity.getDescription(), entity.isEarlyAccess(),
                entity.getDateOfCreation(), entity.getDateOfModification(), entity.getImageUrls());
    }

    public static PurchasedProductResponse toResponse(PurchasedProductEntity entity) {
        ProductEntity product = entity.getProduct();
        return new PurchasedProductResponse(product.getName(), product.getPrice(), entity.getQuantity());
    }

    public static OrderResponse toResponse(OrderEntity entity) {
        Set<PurchasedProductResponse> purchasedProducts = convertPurchasedEntitiesToResponses(entity.getPurchasedProducts());
        return new OrderResponse(entity.getCartKey(), purchasedProducts, entity.getTotalPrice(), entity.getDate(),
                entity.getStatus().getValue(), entity.getUser().getUsername());
    }

    public static ProductResponse toProductResponse(ProductToBuyEntity toBuyEntity) {
        return toResponse(toBuyEntity.getProduct());
    }

    public static ProductEntity toEntity(ProductDto productDto) {
        return new ProductEntity(null, productDto.getName(), productDto.getPrice(), productDto.getStocks(),
                ProductCategory.getKeyByValue(productDto.getCategory()), ProductType.getKeyByValue(productDto.getType()),
                productDto.getBrand(), productDto.getModel(), productDto.getDescription(), productDto.isEarlyAccess(),
                null, null, new HashSet<>(), null, new HashSet<>(), new HashSet<>());
    }

    public static UserEntity toEntity(UserDto user) {
        return new UserEntity(null, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getAddress(),
                user.getUsername(), user.getPassword(), UserRole.getKeyByValue(user.getRole()), false, null, null,
                null, new HashSet<>());
    }

    public static Set<ProductToBuyResponse> convertEntitiesToResponses(Set<ProductToBuyEntity> entities) {
        return entities.stream().map(ModelConverter::toResponse)
                .sorted(comparing(toBuyResponse -> toBuyResponse.getProduct().getPrice(), reverseOrder()))
                .collect(toCollection(LinkedHashSet::new));
    }

    public static Set<PurchasedProductResponse> convertPurchasedEntitiesToResponses(Set<PurchasedProductEntity> entities) {
        return entities.stream().map(ModelConverter::toResponse).collect(toSet());
    }
}
