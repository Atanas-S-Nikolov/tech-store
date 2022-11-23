package com.techstore.exception.handler;

import com.techstore.exception.ProductConstraintViolationException;
import com.techstore.exception.ProductImageUploaderServiceException;
import com.techstore.exception.ProductNotFoundException;
import com.techstore.exception.ProductServiceException;
import com.techstore.model.response.ErrorResponse;
import com.techstore.model.response.builder.ErrorResponseBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice(basePackages = {"com.techstore.controller"})
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ProductConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ProductConstraintViolationException exception) {
        return ErrorResponseBuilder.buildErrorResponse(CONFLICT, exception);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        return ErrorResponseBuilder.buildErrorResponse(NOT_FOUND, exception);
    }

    @ExceptionHandler(value = {ProductServiceException.class, ProductImageUploaderServiceException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleServiceAndUnknownException(RuntimeException exception) {
        return ErrorResponseBuilder.buildErrorResponse(INTERNAL_SERVER_ERROR, "Internal server error");
    }
}
