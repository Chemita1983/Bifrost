package com.bifrost.infrastructure.web;

import com.bifrost.infrastructure.web.dto.VideogameRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

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
}
