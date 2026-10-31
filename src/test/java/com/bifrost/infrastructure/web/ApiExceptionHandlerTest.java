package com.bifrost.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {
    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void returnsSafeMessagesForStatusAndMalformedBody() {
        var conflict = handler.status(new ResponseStatusException(HttpStatus.CONFLICT, "Resource is in use",
                new RuntimeException("database secret")));
        assertThat(conflict.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(conflict.getBody()).containsEntry("message", "Resource is in use");
        assertThat(handler.status(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "secret"))
                .getBody()).containsEntry("message", "Internal server error");
        assertThat(handler.malformedBody().getBody()).containsEntry("message", "Invalid request body");
    }
}
