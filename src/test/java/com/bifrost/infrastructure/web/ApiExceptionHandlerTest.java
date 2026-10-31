package com.bifrost.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void validationSortsAndCombinesFieldErrors() {
        var binding = new BeanPropertyBindingResult(new Object(), "request");
        binding.addError(new FieldError("request", "zField", "last"));
        binding.addError(new FieldError("request", "aField", "first"));
        var exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(binding);

        var response = handler.validation(exception);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsOnlyKeys("message")
                .containsEntry("message", "aField: first; zField: last");
    }

    @Test
    void statusUsesReasonPhraseWhenClientErrorHasNoReasonAndHidesServerErrors() {
        var notFound = handler.status(new ResponseStatusException(HttpStatus.NOT_FOUND));
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(notFound.getBody()).containsEntry("message", "Not Found");

        var unavailable = handler.status(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE));
        assertThat(unavailable.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(unavailable.getBody()).containsEntry("message", "Internal server error");
    }

    @Test
    void statusHandlesUnknownHttpCodesWithoutReason() {
        var response = handler.status(new ResponseStatusException(HttpStatusCode.valueOf(499)));
        assertThat(response.getStatusCode().value()).isEqualTo(499);
        assertThat(response.getBody()).containsEntry("message", "HTTP 499");
    }
}
