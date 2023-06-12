package com.techstore.exception.mail;

public class MailServiceException extends RuntimeException {
    public MailServiceException(String message) {
        super(message);
    }

    public MailServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailServiceException(Throwable cause) {
        super(cause);
    }
}
