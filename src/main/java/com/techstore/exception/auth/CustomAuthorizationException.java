package com.techstore.exception.auth;

public class CustomAuthorizationException extends RuntimeException {
    public CustomAuthorizationException(String message) {
        super(message);
    }

    public CustomAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
