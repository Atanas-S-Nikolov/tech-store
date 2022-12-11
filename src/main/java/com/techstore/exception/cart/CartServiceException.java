package com.techstore.exception.cart;

public class CartServiceException extends RuntimeException {
    public CartServiceException() {
    }

    public CartServiceException(String message) {
        super(message);
    }

    public CartServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
