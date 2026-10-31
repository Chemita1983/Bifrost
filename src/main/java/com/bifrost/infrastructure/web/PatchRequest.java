package com.bifrost.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** A map preserves the distinction between absent properties and explicit nulls. */
final class PatchRequest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Set<String> NULLABLE_RELATIONS = Set.of("platformId", "digitalPlatformId");

    private PatchRequest() { }

    static void validate(Map<String, Object> body, Class<?> type, Validator validator) {
        Map<String, RecordComponent> fields = Arrays.stream(type.getRecordComponents())
                .collect(Collectors.toMap(RecordComponent::getName, component -> component));
        body.forEach((name, value) -> {
            RecordComponent field = fields.get(name);
            if (field == null) throw invalid(name);
            if (value == null) {
                if (!NULLABLE_RELATIONS.contains(name)) throw invalid(name);
                return;
            }
            Class<?> fieldType = field.getType();
            boolean valid = fieldType == String.class ? value instanceof String
                    : fieldType == int.class || fieldType == Integer.class ? value instanceof Integer
                    : fieldType == boolean.class || fieldType == Boolean.class ? value instanceof Boolean : false;
            if (!valid || !validator.validateValue(type, name, value).isEmpty()) {
                throw invalid(name);
            }
        });
    }

    static <T> T merge(Map<String, Object> body, T current, Class<T> type) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values = MAPPER.convertValue(current, Map.class);
        values.putAll(body);
        return MAPPER.convertValue(values, type);
    }

    private static ResponseStatusException invalid(String field) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid field: " + field);
    }
}
