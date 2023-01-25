package com.techstore.exception.product;

public class DeleteProductImageException extends RuntimeException {
    public DeleteProductImageException() {
        super();
    }

    public DeleteProductImageException(String message) {
        super(message);
    }

    public DeleteProductImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
