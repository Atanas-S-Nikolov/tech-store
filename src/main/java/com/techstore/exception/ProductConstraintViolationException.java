package com.techstore.exception;

public class ProductConstraintViolationException extends RuntimeException {
    public ProductConstraintViolationException() {
    }

    public ProductConstraintViolationException(String message) {
        super(message);
    }

    public ProductConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
