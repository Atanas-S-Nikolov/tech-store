package com.techstore.exception.product;

public class ProductImageUploaderServiceException extends RuntimeException {
    public ProductImageUploaderServiceException() {
    }

    public ProductImageUploaderServiceException(String message) {
        super(message);
    }

    public ProductImageUploaderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
