package com.bifrost.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;

public record DigitalPlatformUpdateRequest(
        @JsonProperty(value = "digitalPlatformName", required = true) @JsonDeserialize(using = StrictValues.Text.class) String digitalPlatformName,
        @JsonProperty(value = "digitalPlatformCompany", required = true) @JsonDeserialize(using = StrictValues.Text.class) String digitalPlatformCompany) {
    public DigitalPlatformUpdateRequest {
        Objects.requireNonNull(digitalPlatformName, "digitalPlatformName is required");
        Objects.requireNonNull(digitalPlatformCompany, "digitalPlatformCompany is required");
    }

    public DigitalPlatformRequest toRequest() {
        return new DigitalPlatformRequest(digitalPlatformName, digitalPlatformCompany);
    }
}
