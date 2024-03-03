package com.bifrost.domain.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message) {
        super(message);
    }
}
