package com.techstore.exception.product;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException() {
    }

    public ProductServiceException(String message) {
        super(message);
    }

    public ProductServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
