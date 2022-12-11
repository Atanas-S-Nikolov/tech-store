package com.techstore.exception.cart;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
    }

    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
