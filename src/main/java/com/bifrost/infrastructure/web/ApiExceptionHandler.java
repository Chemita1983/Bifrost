package com.bifrost.infrastructure.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Turns exceptions into {"message": "..."} responses.
 * Server errors (5xx) are logged and never expose internal details.
 */
@RestControllerAdvice
class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> validation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted().collect(Collectors.joining("; "));
        return response(HttpStatus.BAD_REQUEST, HttpHeaders.EMPTY, message, exception);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, String>> malformedBody(HttpMessageNotReadableException exception) {
        return response(HttpStatus.BAD_REQUEST, HttpHeaders.EMPTY, "Invalid request body", exception);
    }

    /** For example, text in a numeric path variable such as {@code /platform/abc}. */
    @ExceptionHandler(TypeMismatchException.class)
    ResponseEntity<Map<String, String>> typeMismatch(TypeMismatchException exception) {
        if (exception instanceof ConversionNotSupportedException) {
            return unexpected(exception);
        }
        return response(HttpStatus.BAD_REQUEST, HttpHeaders.EMPTY, "Invalid parameter value", exception);
    }

    /** For example, deleting a platform that videogames still reference. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Map<String, String>> conflict(DataIntegrityViolationException exception) {
        return response(HttpStatus.CONFLICT, HttpHeaders.EMPTY, "Resource is in use", exception);
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Map<String, String>> status(ResponseStatusException exception) {
        return response(exception.getStatusCode(), exception.getHeaders(), exception.getReason(), exception);
    }

    /** Keeps the status of Spring errors (404, 405, 415...) and returns 500 for anything else. */
    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, String>> unexpected(Exception exception) {
        if (exception instanceof ErrorResponse error) {
            return response(error.getStatusCode(), error.getHeaders(), null, exception);
        }
        ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        HttpStatusCode status = annotation != null ? annotation.code() : HttpStatus.INTERNAL_SERVER_ERROR;
        return response(status, HttpHeaders.EMPTY, null, exception);
    }

    private ResponseEntity<Map<String, String>> response(HttpStatusCode status, HttpHeaders headers,
                                                         String message, Exception exception) {
        if (status.is5xxServerError()) {
            log.error("Unexpected error while handling request", exception);
            message = "Internal server error";
        } else if (message == null) {
            HttpStatus knownStatus = HttpStatus.resolve(status.value());
            message = knownStatus != null ? knownStatus.getReasonPhrase() : "HTTP " + status.value();
        }
        return ResponseEntity.status(status).headers(headers).body(Map.of("message", message));
    }
}
