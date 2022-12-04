package com.techstore.exception.handler;

import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.exception.product.ProductConstraintViolationException;
import com.techstore.exception.product.ProductImageUploaderServiceException;
import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.exception.product.ProductServiceException;
import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserServiceException;
import com.techstore.model.response.ErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.techstore.model.response.builder.ErrorResponseBuilder.buildErrorResponse;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice(basePackages = {"com.techstore.controller"})
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ProductConstraintViolationException.class, UserConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(RuntimeException exception) {
        return buildErrorResponse(CONFLICT, exception);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        return buildErrorResponse(NOT_FOUND, exception);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(InvalidCredentialsException exception) {
        return buildErrorResponse(UNAUTHORIZED, exception);
    }

    @ExceptionHandler(value = {InvalidJWTException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(InvalidJWTException exception) {
        return buildErrorResponse(FORBIDDEN, exception);
    }

    @ExceptionHandler(value = {ProductServiceException.class, ProductImageUploaderServiceException.class,
            UserServiceException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleServiceAndUnknownException(RuntimeException exception) {
        return buildErrorResponse(INTERNAL_SERVER_ERROR, "Internal server error");
    }
}
