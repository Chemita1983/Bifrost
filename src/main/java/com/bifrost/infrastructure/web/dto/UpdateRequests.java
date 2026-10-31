package com.bifrost.infrastructure.web.dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Parses complete PUT representations without changing the more permissive POST DTOs. */
public final class UpdateRequests {
    private UpdateRequests() {
    }

    public static PlatformRequest platform(JsonNode body) {
        return new PlatformRequest(text(body, "platformName"), text(body, "platformCompany"),
                text(body, "platformYear"));
    }

    public static DigitalPlatformRequest digitalPlatform(JsonNode body) {
        return new DigitalPlatformRequest(text(body, "digitalPlatformName"),
                text(body, "digitalPlatformCompany"));
    }

    public static VideogameRequest videogame(JsonNode body) {
        return new VideogameRequest(text(body, "videogameName"), text(body, "videogameType"),
                text(body, "videogameYear"), optionalId(body, "platformId"),
                optionalId(body, "digitalPlatformId"), text(body, "companyName"),
                integer(body, "isDigital"), integer(body, "isCompleted"),
                integer(body, "isPlatinum"), bool(body, "multiplayer"));
    }

    private static JsonNode field(JsonNode body, String name) {
        if (!body.isObject() || !body.has(name)) {
            throw invalid(name);
        }
        return body.get(name);
    }

    private static String text(JsonNode body, String name) {
        JsonNode value = field(body, name);
        if (!value.isTextual()) {
            throw invalid(name);
        }
        return value.textValue();
    }

    private static int integer(JsonNode body, String name) {
        JsonNode value = field(body, name);
        if (!value.isIntegralNumber() || !value.canConvertToInt()) {
            throw invalid(name);
        }
        return value.intValue();
    }

    private static Integer optionalId(JsonNode body, String name) {
        JsonNode value = field(body, name);
        if (value.isNull()) {
            return null;
        }
        return integer(body, name);
    }

    private static boolean bool(JsonNode body, String name) {
        JsonNode value = field(body, name);
        if (!value.isBoolean()) {
            throw invalid(name);
        }
        return value.booleanValue();
    }

    private static ResponseStatusException invalid(String name) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing or invalid field: " + name);
    }
}
