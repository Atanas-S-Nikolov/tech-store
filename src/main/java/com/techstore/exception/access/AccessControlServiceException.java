package com.techstore.exception.access;

public class AccessControlServiceException extends RuntimeException {
    public AccessControlServiceException() {
    }

    public AccessControlServiceException(String message) {
        super(message);
    }

    public AccessControlServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
