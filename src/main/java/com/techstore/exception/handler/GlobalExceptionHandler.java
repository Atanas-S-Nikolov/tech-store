package com.techstore.exception.handler;

import com.techstore.exception.authentication.CustomAuthenticationException;
import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.favorites.FavoritesNotFoundException;
import com.techstore.exception.mail.MailServiceException;
import com.techstore.exception.order.OrderNotFoundException;
import com.techstore.exception.order.OrderServiceException;
import com.techstore.exception.product.CannotBuyProductException;
import com.techstore.exception.product.DeleteProductImageException;
import com.techstore.exception.product.ProductImageUploaderServiceException;
import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.exception.product.UploadProductImageException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserNotFoundException;
import com.techstore.model.response.ValidationErrorResponse.RejectedValue;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice(basePackages = {"com.techstore.controller"})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    //4xx
    @ExceptionHandler(value = {ConstraintViolationException.class, CannotBuyProductException.class})
    public ResponseEntity<Object> handleBadRequest(RuntimeException exception) {
        logError(exception);
        return buildErrorResponse(BAD_REQUEST, exception);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity<Object> handleUnauthorized(InvalidCredentialsException exception) {
        logError(exception);
        return buildErrorResponse(UNAUTHORIZED, exception);
    }

    @ExceptionHandler(value = {InvalidJWTException.class, AccessDeniedException.class, CustomAuthenticationException.class})
    public ResponseEntity<Object> handleForbidden(RuntimeException exception) {
        logError(exception);
        return buildErrorResponse(FORBIDDEN, exception);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class, UserNotFoundException.class, CartNotFoundException.class,
            FavoritesNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException exception) {
        logError(exception);
        return buildErrorResponse(NOT_FOUND, exception);
    }

    @ExceptionHandler(value = UserConstraintViolationException.class)
    public ResponseEntity<Object> handleCustomConstraintViolationException(RuntimeException exception) {
        logError(exception);
        return buildErrorResponse(CONFLICT, exception);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        logError(exception);
        return buildErrorResponse(CONFLICT, "Database constraint violation. Maybe you are trying to create entity which already exists or delete entity with relations");
    }

    //5xx
    @ExceptionHandler(value = {DataAccessException.class, HibernateException.class})
    public ResponseEntity<Object> handleDatabaseExceptions(RuntimeException exception) {
        logError(exception);
        return buildErrorResponse(INTERNAL_SERVER_ERROR, "Error while connecting to the database");
    }

    @ExceptionHandler(value = {UploadProductImageException.class, DeleteProductImageException.class, })
    public ResponseEntity<Object> handleProductImageExceptions(RuntimeException exception) {
        logError(exception);
        return buildErrorResponse(INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(value = {ProductImageUploaderServiceException.class, OrderServiceException.class, MailServiceException.class,
            Exception.class})
    public ResponseEntity<Object> handleServiceAndUnknownExceptions(Exception exception) {
        logError(exception);
        return buildErrorResponse(INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logError(exception);
        Set<String> globalMessages = new HashSet<>();
        List<RejectedValue> rejectedValues = new ArrayList<>();

        BindingResult bindingResult = exception.getBindingResult();

        if (bindingResult.hasFieldErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String property = fieldError.getField();
                String value = fieldError.getRejectedValue().toString();
                String message = fieldError.getDefaultMessage();
                rejectedValues.add(new RejectedValue(message, property, value));
            }
        }
        if (bindingResult.hasGlobalErrors()) {
            for (ObjectError globalError : bindingResult.getGlobalErrors()) {
                globalMessages.add(globalError.getDefaultMessage());
            }
        }

        return buildValidationErrorResponse(status, globalMessages, rejectedValues);
    }

    private void logError(Exception exception) {
        log.error(exception.getMessage(), exception);
    }
}
