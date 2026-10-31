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
    void hoursCanBePreservedSetToZeroOrClearedAndMustBeNonNegativeIntegers() {
        var current = new VideogameRequest("Game", null, null, null, null, null, 0, 0, 0, false, 12);
        assertThat(PatchRequest.merge(Map.of(), current).hoursPlayed()).isEqualTo(12);
        PatchRequest.validateVideogame(Map.of("hoursPlayed", 0), validator);
        assertThat(PatchRequest.merge(Map.of("hoursPlayed", 0), current).hoursPlayed()).isZero();
        Map<String, Object> clear = new HashMap<>();
        clear.put("hoursPlayed", null);
        PatchRequest.validateVideogame(clear, validator);
        assertThat(PatchRequest.merge(clear, current).hoursPlayed()).isNull();
        for (Object invalid : List.of(-1, 1.5, "1", 2147483648L)) {
            assertThatThrownBy(() -> PatchRequest.validateVideogame(Map.of("hoursPlayed", invalid), validator))
                    .isInstanceOf(ResponseStatusException.class);
        }
        assertThat(validator.validate(new VideogameRequest("Game", null, null, null, null, null, 0, 0, 0, false, -1)))
                .extracting(v -> v.getPropertyPath().toString()).contains("hoursPlayed");
    }

    @Test
    void platformPatchValidatesAndMergesOnlySuppliedFields() {
        PlatformRequest current = new PlatformRequest("Old", "Company", "2000");
        Map<String, Object> body = Map.of("platformName", "New", "platformYear", "2024");
        PatchRequest.validatePlatform(body, validator);
        assertThat(PatchRequest.merge(body, current)).isEqualTo(new PlatformRequest("New", "Company", "2024"));
        assertThatThrownBy(() -> PatchRequest.validatePlatform(Map.of("platformName", " "), validator))
                .isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> PatchRequest.validatePlatform(Map.of("unknown", "x"), validator))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void digitalPlatformPatchValidatesAndMergesOnlySuppliedFields() {
        DigitalPlatformRequest current = new DigitalPlatformRequest("Old", "Company");
        Map<String, Object> body = Map.of("digitalPlatformCompany", "New company");
        PatchRequest.validateDigitalPlatform(body, validator);
        assertThat(PatchRequest.merge(body, current)).isEqualTo(new DigitalPlatformRequest("Old", "New company"));
        assertThatThrownBy(() -> PatchRequest.validateDigitalPlatform(Map.of("digitalPlatformName", " "), validator))
                .isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> PatchRequest.validateDigitalPlatform(Map.of("digitalPlatformCompany", false), validator))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void validatesOnlySuppliedValuesAndAllowsClearingRelations() {
        Map<String, Object> body = new HashMap<>();
        body.put("platformId", null);
        body.put("isDigital", 0);
        PatchRequest.validateVideogame(body, validator);
        VideogameRequest current = new VideogameRequest("Game", null, null, 2, null, null, 1, 0, 0, false);
        VideogameRequest merged = PatchRequest.merge(body, current);
        assertThat(merged.videogameName()).isEqualTo("Game");
        assertThat(merged.platformId()).isNull();
        assertThat(merged.isDigital()).isZero();
    }

    @Test
    void rejectsInvalidSuppliedValues() {
        for (Map<String, Object> body : List.<Map<String, Object>>of(Map.of("videogameName", " "), Map.of("platformId", 0),
                Map.of("isDigital", 2), Map.of("unknown", 1), Map.of("isDigital", "1"))) {
            assertThatThrownBy(() -> PatchRequest.validateVideogame(body, validator))
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
            assertThatThrownBy(() -> PatchRequest.validateVideogame(example.getKey(), validator))
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
                PatchRequest.validateVideogame(Map.of(field, value), validator);
            }
            for (int value : List.of(-1, 2)) {
                assertThatThrownBy(() -> PatchRequest.validateVideogame(Map.of(field, value), validator))
                        .isInstanceOf(ResponseStatusException.class);
            }
        }
        PatchRequest.validateVideogame(Map.of("platformId", 1, "digitalPlatformId", 1, "multiplayer", false), validator);
        Map<String, Object> relations = new HashMap<>();
        relations.put("platformId", null);
        relations.put("digitalPlatformId", null);
        PatchRequest.validateVideogame(relations, validator);
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
