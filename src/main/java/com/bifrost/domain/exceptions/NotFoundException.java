package com.bifrost.domain.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(final String message) {
        super(message);
    }
}