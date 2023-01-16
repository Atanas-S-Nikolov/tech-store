package com.techstore.exception.cart;

public class CartConstraintViolationException extends RuntimeException {
    public CartConstraintViolationException() {
    }

    public CartConstraintViolationException(String message) {
        super(message);
    }

    public CartConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
