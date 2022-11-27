package com.techstore.exception.user;

public class UserConstraintViolationException extends RuntimeException {
    public UserConstraintViolationException() {
    }

    public UserConstraintViolationException(String message) {
        super(message);
    }

    public UserConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
