package com.techstore.model.response;

import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ValidationErrorResponse extends ErrorResponse{
    private final List<RejectedValue> rejectedProperties;

    public ValidationErrorResponse(HttpStatus status, Collection<String> messages, List<RejectedValue> rejectedProperties) {
        super(status, messages);
        this.rejectedProperties = rejectedProperties;
    }

    public List<RejectedValue> getRejectedProperties() {
        return rejectedProperties;
    }

    @Override
    public String toString() {
        return "ValidationErrorResponse{" +
                "rejectedProperties=" + rejectedProperties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationErrorResponse)) return false;
        if (!super.equals(o)) return false;
        ValidationErrorResponse that = (ValidationErrorResponse) o;
        return Objects.equals(rejectedProperties, that.rejectedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rejectedProperties);
    }

    public static class RejectedValue {
        private final String message;
        private final String property;
        private final String value;

        public RejectedValue(String message, String property, String value) {
            this.message = message;
            this.property = property;
            this.value = value;
        }

        public String getMessage() {
            return message;
        }

        public String getProperty() {
            return property;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "RejectedValue{" +
                    "message='" + message + '\'' +
                    ", property='" + property + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RejectedValue)) return false;
            RejectedValue that = (RejectedValue) o;
            return Objects.equals(message, that.message) && Objects.equals(property, that.property) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, property, value);
        }
    }
}
