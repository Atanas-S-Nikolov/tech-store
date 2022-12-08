package com.techstore.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class ErrorResponse {
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd-HH-mm-ss-ns")
    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final Collection<String> messages;

    public ErrorResponse(HttpStatus status, Collection<String> messages) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.messages = messages;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Collection<String> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(timestamp, that.timestamp) && status == that.status && Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, messages);
    }
}
