package com.techstore.exception.favorites;

public class FavoritesNotFoundException extends RuntimeException {
    public FavoritesNotFoundException() {
        super();
    }

    public FavoritesNotFoundException(String message) {
        super(message);
    }

    public FavoritesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
