package com.bifrost.infrastructure.web;

import com.bifrost.infrastructure.web.dto.DigitalPlatformRequest;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import com.bifrost.infrastructure.web.dto.VideogameRequest;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/** The map distinguishes an omitted property from an explicit null. */
final class PatchRequest {
    private PatchRequest() { }

    static void validatePlatform(Map<String, Object> body, Validator validator) {
        body.forEach((name, value) -> {
            switch (name) {
                case "platformName", "platformCompany", "platformYear" ->
                        validateField(PlatformRequest.class, name, value, String.class, false, validator);
                default -> throw invalid(name);
            }
        });
    }

    static void validateDigitalPlatform(Map<String, Object> body, Validator validator) {
        body.forEach((name, value) -> {
            switch (name) {
                case "digitalPlatformName", "digitalPlatformCompany" ->
                        validateField(DigitalPlatformRequest.class, name, value, String.class, false, validator);
                default -> throw invalid(name);
            }
        });
    }

    static void validateVideogame(Map<String, Object> body, Validator validator) {
        body.forEach((name, value) -> {
            switch (name) {
                case "videogameName", "videogameType", "videogameYear", "companyName" ->
                        validateField(VideogameRequest.class, name, value, String.class, false, validator);
                case "platformId", "digitalPlatformId" ->
                        validateField(VideogameRequest.class, name, value, Integer.class, true, validator);
                case "isDigital", "isCompleted", "isPlatinum" ->
                        validateField(VideogameRequest.class, name, value, Integer.class, false, validator);
                case "multiplayer" ->
                        validateField(VideogameRequest.class, name, value, Boolean.class, false, validator);
                default -> throw invalid(name);
            }
        });
    }

    private static void validateField(Class<?> requestType, String name, Object value, Class<?> valueType,
                                      boolean nullable, Validator validator) {
        if (value == null) {
            if (!nullable) throw invalid(name);
        } else if (!valueType.isInstance(value) || !validator.validateValue(requestType, name, value).isEmpty()) {
            throw invalid(name);
        }
    }

    static PlatformRequest merge(Map<String, Object> body, PlatformRequest current) {
        return new PlatformRequest(
                (String) body.getOrDefault("platformName", current.platformName()),
                (String) body.getOrDefault("platformCompany", current.platformCompany()),
                (String) body.getOrDefault("platformYear", current.platformYear()));
    }

    static DigitalPlatformRequest merge(Map<String, Object> body, DigitalPlatformRequest current) {
        return new DigitalPlatformRequest(
                (String) body.getOrDefault("digitalPlatformName", current.digitalPlatformName()),
                (String) body.getOrDefault("digitalPlatformCompany", current.digitalPlatformCompany()));
    }

    static VideogameRequest merge(Map<String, Object> body, VideogameRequest current) {
        return new VideogameRequest(
                (String) body.getOrDefault("videogameName", current.videogameName()),
                (String) body.getOrDefault("videogameType", current.videogameType()),
                (String) body.getOrDefault("videogameYear", current.videogameYear()),
                (Integer) body.getOrDefault("platformId", current.platformId()),
                (Integer) body.getOrDefault("digitalPlatformId", current.digitalPlatformId()),
                (String) body.getOrDefault("companyName", current.companyName()),
                (Integer) body.getOrDefault("isDigital", current.isDigital()),
                (Integer) body.getOrDefault("isCompleted", current.isCompleted()),
                (Integer) body.getOrDefault("isPlatinum", current.isPlatinum()),
                (Boolean) body.getOrDefault("multiplayer", current.multiplayer()));
    }

    private static ResponseStatusException invalid(String field) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid field: " + field);
    }
}
