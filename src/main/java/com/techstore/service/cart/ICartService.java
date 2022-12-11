package com.techstore.service.cart;

import com.techstore.model.Cart;
import com.techstore.model.dto.CartDto;

public interface ICartService {
    Cart getCart(String username);

    Cart addProductsToCart(CartDto cartDto);

    void deleteCart(String username);
}
