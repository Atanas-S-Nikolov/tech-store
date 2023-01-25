package com.techstore.exception.product;

public class UploadProductImageException extends RuntimeException {
    public UploadProductImageException() {
        super();
    }

    public UploadProductImageException(String message) {
        super(message);
    }

    public UploadProductImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
