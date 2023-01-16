package com.techstore.service.cart;

import com.techstore.model.Cart;
import com.techstore.model.dto.CartDto;
import com.techstore.model.entity.CartEntity;

public interface ICartService {
    CartEntity createDefaultCart();

    Cart getCart(String username);

    Cart updateCart(CartDto cartDto);

    Cart clearCart(String username);

    void deleteCart(String username);
}
