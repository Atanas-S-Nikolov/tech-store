package com.techstore.exception.authentication;

public class InvalidJWTException extends RuntimeException {
    public InvalidJWTException() {
    }

    public InvalidJWTException(String message) {
        super(message);
    }

    public InvalidJWTException(String message, Throwable cause) {
        super(message, cause);
    }
}
