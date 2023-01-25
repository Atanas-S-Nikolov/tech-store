package com.techstore.service.cart;

import com.techstore.exception.cart.CartConstraintViolationException;
import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.model.Cart;
import com.techstore.model.dto.CartDto;
import com.techstore.model.entity.CartEntity;

public interface ICartService {
    CartEntity createDefaultCart() throws CartConstraintViolationException;

    Cart getCart(String username) throws CartNotFoundException;

    Cart updateCart(CartDto cartDto) throws CartNotFoundException, CartConstraintViolationException;

    Cart clearCart(String username) throws CartNotFoundException, CartConstraintViolationException;

    void deleteCart(String username) throws CartConstraintViolationException;
}
