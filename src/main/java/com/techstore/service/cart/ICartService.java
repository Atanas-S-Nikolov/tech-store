package com.techstore.service.cart;

import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.model.dto.CartDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.ProductToBuyEntity;
import com.techstore.model.entity.PurchasedProductEntity;
import com.techstore.model.response.CartResponse;
import com.techstore.model.dto.UpdateCartDto;

import java.util.Set;

public interface ICartService {
    CartResponse createCart(CartDto updateCartDto);

    CartResponse getCart(String key) throws CartNotFoundException;

    CartResponse addProductToCart(UpdateCartDto updateCartDto) throws CartNotFoundException;

    CartResponse removeProductFromCart(UpdateCartDto updateCartDto) throws CartNotFoundException;

    CartResponse doPurchase(String key) throws CartNotFoundException;

    CartResponse clearCart(String key) throws CartNotFoundException;

    void deleteCart(String key) throws CartNotFoundException;

    CartEntity findCartEntity(String key);

    void decreaseStocks(Set<ProductToBuyEntity> productsToBuy);

    void increaseStocks(Set<PurchasedProductEntity> purchasedProducts);
}
