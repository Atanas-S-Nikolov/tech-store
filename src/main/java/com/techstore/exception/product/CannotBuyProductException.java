package com.techstore.exception.product;

public class CannotBuyProductException extends RuntimeException {
    public CannotBuyProductException() {
        super();
    }

    public CannotBuyProductException(String message) {
        super(message);
    }

    public CannotBuyProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
