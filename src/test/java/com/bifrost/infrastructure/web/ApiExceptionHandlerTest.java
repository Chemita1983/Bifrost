package com.bifrost.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.http.MediaType;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void unexpectedErrorsAreSanitizedAndSpringErrorsKeepTheirStatusAndHeaders() {
        var response = handler.unexpected(new IllegalStateException("secret"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsOnlyKeys("message").containsEntry("message", "Internal server error");

        var method = handler.unexpected(new HttpRequestMethodNotSupportedException("POST", java.util.List.of("GET")));
        assertThat(method.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(method.getHeaders().getAllow()).containsExactly(org.springframework.http.HttpMethod.GET);
        assertThat(handler.unexpected(new AnnotatedException()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void mvcSanitizesUnexpectedErrorsAndPreservesFrameworkStatuses() throws Exception {
        var mvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(handler).build();
        mvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"message\":\"Internal server error\"}", true));
        mvc.perform(get("/test/missing"))
                .andExpect(status().isNotFound());
        mvc.perform(put("/test/body"))
                .andExpect(status().isMethodNotAllowed()).andExpect(header().exists("Allow"));
        mvc.perform(post("/test/body").contentType(MediaType.TEXT_PLAIN).content("secret"))
                .andExpect(status().isUnsupportedMediaType());
        mvc.perform(get("/test/annotated"))
                .andExpect(status().isNotFound());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class AnnotatedException extends RuntimeException { }

    @RestController
    static class TestController {
        @GetMapping("/test/error")
        String error() { throw new IllegalStateException("sensitive exception details"); }

        @GetMapping("/test/annotated")
        String annotated() { throw new AnnotatedException(); }

        @PostMapping(value = "/test/body", consumes = MediaType.APPLICATION_JSON_VALUE)
        String body(@RequestBody String body) { return body; }
    }
}
