package com.bifrost.infrastructure.web;

import com.bifrost.infrastructure.web.dto.VideogameRequest;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import com.bifrost.infrastructure.web.dto.DigitalPlatformRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PatchRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validatesOnlySuppliedValuesAndAllowsClearingRelations() {
        Map<String, Object> body = new HashMap<>();
        body.put("platformId", null);
        body.put("isDigital", 0);
        PatchRequest.validate(body, VideogameRequest.class, validator);
        VideogameRequest current = new VideogameRequest("Game", null, null, 2, null, null, 1, 0, 0, false);
        VideogameRequest merged = PatchRequest.merge(body, current, VideogameRequest.class);
        assertThat(merged.videogameName()).isEqualTo("Game");
        assertThat(merged.platformId()).isNull();
        assertThat(merged.isDigital()).isZero();
    }

    @Test
    void rejectsInvalidSuppliedValues() {
        for (Map<String, Object> body : List.<Map<String, Object>>of(Map.of("videogameName", " "), Map.of("platformId", 0),
                Map.of("isDigital", 2), Map.of("unknown", 1), Map.of("isDigital", "1"))) {
            assertThatThrownBy(() -> PatchRequest.validate(body, VideogameRequest.class, validator))
                    .isInstanceOf(ResponseStatusException.class);
        }
    }

    @Test
    void reportsInvalidFieldForNullWrongTypeUnknownAndConstraintViolation() {
        Map<String, Object> explicitNull = new HashMap<>();
        explicitNull.put("companyName", null);
        for (var example : List.of(
                Map.entry(explicitNull, "companyName"),
                Map.entry(Map.<String, Object>of("multiplayer", 1), "multiplayer"),
                Map.entry(Map.<String, Object>of("isDigital", 1L), "isDigital"),
                Map.entry(Map.<String, Object>of("unknown", 1), "unknown"),
                Map.entry(Map.<String, Object>of("digitalPlatformId", 0), "digitalPlatformId"))) {
            assertThatThrownBy(() -> PatchRequest.validate(example.getKey(), VideogameRequest.class, validator))
                    .isInstanceOfSatisfying(ResponseStatusException.class, exception -> {
                        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                        assertThat(exception.getReason()).isEqualTo("Invalid field: " + example.getValue());
                    });
        }
    }

    @Test
    void acceptsBoundaryValuesAndRejectsOutOfRangeValues() {
        for (String field : List.of("isDigital", "isCompleted", "isPlatinum")) {
            for (int value : List.of(0, 1)) {
                PatchRequest.validate(Map.of(field, value), VideogameRequest.class, validator);
            }
            for (int value : List.of(-1, 2)) {
                assertThatThrownBy(() -> PatchRequest.validate(Map.of(field, value), VideogameRequest.class, validator))
                        .isInstanceOf(ResponseStatusException.class);
            }
        }
        PatchRequest.validate(Map.of("platformId", 1, "digitalPlatformId", 1, "multiplayer", false),
                VideogameRequest.class, validator);
        Map<String, Object> relations = new HashMap<>();
        relations.put("platformId", null);
        relations.put("digitalPlatformId", null);
        PatchRequest.validate(relations, VideogameRequest.class, validator);
    }

    @Test
    void requestDtosValidateNamesAndVideogameBoundaries() {
        assertThat(validator.validate(new PlatformRequest(" ", null, null))).isNotEmpty();
        assertThat(validator.validate(new DigitalPlatformRequest(null, null))).isNotEmpty();
        assertThat(validator.validate(new VideogameRequest("Game", null, null, 1, 1, null, 0, 1, 0, false))).isEmpty();
        var violations = validator.validate(new VideogameRequest(" ", null, null, 0, -1, null, -1, 2, 2, false));
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("videogameName", "platformId", "digitalPlatformId", "isDigital", "isCompleted", "isPlatinum");
    }
}
