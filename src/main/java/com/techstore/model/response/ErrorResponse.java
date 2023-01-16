package com.techstore.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class ErrorResponse {
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd-HH-mm-ss-ns")
    @Getter
    private final LocalDateTime timestamp;

    @Getter
    private final HttpStatus status;

    @Getter
    private final Collection<String> messages;

    public ErrorResponse(HttpStatus status, Collection<String> messages) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.messages = messages;
    }
}
