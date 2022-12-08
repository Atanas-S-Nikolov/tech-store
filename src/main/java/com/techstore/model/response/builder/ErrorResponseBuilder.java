package com.techstore.model.response.builder;

import com.techstore.model.response.ErrorResponse;
import com.techstore.model.response.ValidationErrorResponse;
import com.techstore.model.response.ValidationErrorResponse.RejectedValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ErrorResponseBuilder {
    public static ResponseEntity<Object> buildErrorResponse(HttpStatus status, Exception exception) {
        return buildErrorResponse(status, exception.getMessage());
    }

    public static ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        List<String> messages = Collections.singletonList(message);
        return buildErrorResponse(status, messages);
    }

    public static ResponseEntity<Object> buildErrorResponse(HttpStatus status, Collection<String> messages) {
        return buildResponse(new ErrorResponse(status, messages));
    }

    public static ResponseEntity<Object> buildValidationErrorResponse(HttpStatus status, Collection<String> messages, List<RejectedValue> rejectedValues) {
        return buildResponse(new ValidationErrorResponse(status, messages, rejectedValues));
    }

    private static ResponseEntity<Object> buildResponse(ErrorResponse response) {
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}