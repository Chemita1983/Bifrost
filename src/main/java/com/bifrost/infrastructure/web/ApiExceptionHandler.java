package com.bifrost.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> validation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted().collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, String>> malformedBody() {
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid request body"));
    }

    @ExceptionHandler(TypeMismatchException.class)
    ResponseEntity<Map<String, String>> typeMismatch(TypeMismatchException exception) {
        if (exception instanceof ConversionNotSupportedException) {
            return unexpected(exception);
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid parameter value"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Map<String, String>> status(ResponseStatusException exception) {
        String reason = exception.getReason();
        HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());
        String message = exception.getStatusCode().is5xxServerError() ? "Internal server error"
                : reason != null ? reason : status != null ? status.getReasonPhrase() : "HTTP " + exception.getStatusCode().value();
        if (exception.getStatusCode().is5xxServerError()) {
            log.error("Server error while handling request", exception);
        }
        return ResponseEntity.status(exception.getStatusCode()).headers(exception.getHeaders()).body(Map.of("message", message));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, String>> unexpected(Exception exception) {
        if (exception instanceof ErrorResponse error) {
            HttpStatusCode status = error.getStatusCode();
            if (status.is5xxServerError()) {
                log.error("Server error while handling request", exception);
            }
            return ResponseEntity.status(status).headers(error.getHeaders())
                    .body(Map.of("message", safeMessage(status)));
        }
        ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            HttpStatusCode status = annotation.code();
            if (status.is5xxServerError()) {
                log.error("Server error while handling request", exception);
            }
            return ResponseEntity.status(status).body(Map.of("message", safeMessage(status)));
        }
        log.error("Unexpected error while handling request", exception);
        return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
    }

    private String safeMessage(HttpStatusCode status) {
        HttpStatus knownStatus = HttpStatus.resolve(status.value());
        return status.is5xxServerError() ? "Internal server error"
                : knownStatus != null ? knownStatus.getReasonPhrase() : "HTTP " + status.value();
    }
}
