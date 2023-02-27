package com.techstore.service.cart;

import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.model.response.CartResponse;
import com.techstore.model.dto.CartDto;
import com.techstore.model.entity.CartEntity;

public interface ICartService {
    CartEntity createDefaultCart();

    CartResponse getCart(String username) throws CartNotFoundException;

    CartResponse addProductToCart(CartDto cartDto) throws CartNotFoundException;

    CartResponse removeProductFromCart(CartDto cartDto) throws CartNotFoundException;

    CartResponse doPurchase(String username) throws CartNotFoundException;

    CartResponse clearCart(String username) throws CartNotFoundException;

    void deleteCart(String username) throws CartNotFoundException;
}
