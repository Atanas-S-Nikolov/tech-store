package com.techstore.exception.handler;

import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.exception.product.ProductConstraintViolationException;
import com.techstore.exception.product.ProductImageUploaderServiceException;
import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.exception.product.ProductServiceException;
import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserServiceException;

import com.techstore.model.response.ValidationErrorResponse.RejectedValue;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.techstore.model.response.builder.ErrorResponseBuilder.buildErrorResponse;
import static com.techstore.model.response.builder.ErrorResponseBuilder.buildValidationErrorResponse;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(basePackages = {"com.techstore.controller"})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ProductConstraintViolationException.class, UserConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(RuntimeException exception) {
        return buildErrorResponse(CONFLICT, exception);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException exception) {
        return buildErrorResponse(NOT_FOUND, exception);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity<Object> handleUnauthorized(InvalidCredentialsException exception) {
        return buildErrorResponse(UNAUTHORIZED, exception);
    }

    @ExceptionHandler(value = {InvalidJWTException.class})
    public ResponseEntity<Object> handleForbidden(InvalidJWTException exception) {
        return buildErrorResponse(FORBIDDEN, exception);
    }

    @ExceptionHandler(value = {ProductServiceException.class, ProductImageUploaderServiceException.class,
            UserServiceException.class, Exception.class})
    public ResponseEntity<Object> handleServiceAndUnknownException(RuntimeException exception) {
        return buildErrorResponse(INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        return buildErrorResponse(BAD_REQUEST, exception);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Set<String> messages = new HashSet<>();
        List<RejectedValue> rejectedValues = new ArrayList<>();

        BindingResult bindingResult = ex.getBindingResult();

        if (bindingResult.hasFieldErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String property = fieldError.getField();
                String value = fieldError.getRejectedValue().toString();
                rejectedValues.add(new RejectedValue(property, value));
                messages.add(fieldError.getDefaultMessage());
            }
        } else if (bindingResult.hasGlobalErrors()) {
            for (ObjectError globalError : bindingResult.getGlobalErrors()) {
                messages.add(globalError.getDefaultMessage());
            }
        }

        return buildValidationErrorResponse(status, messages, rejectedValues);
    }
}
