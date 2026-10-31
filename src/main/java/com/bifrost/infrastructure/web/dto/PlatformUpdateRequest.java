package com.bifrost.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;

public record PlatformUpdateRequest(
        @JsonProperty(value = "platformName", required = true) @JsonDeserialize(using = StrictValues.Text.class) String platformName,
        @JsonProperty(value = "platformCompany", required = true) @JsonDeserialize(using = StrictValues.Text.class) String platformCompany,
        @JsonProperty(value = "platformYear", required = true) @JsonDeserialize(using = StrictValues.Text.class) String platformYear) {
    public PlatformUpdateRequest {
        Objects.requireNonNull(platformName, "platformName is required");
        Objects.requireNonNull(platformCompany, "platformCompany is required");
        Objects.requireNonNull(platformYear, "platformYear is required");
    }

    public PlatformRequest toRequest() {
        return new PlatformRequest(platformName, platformCompany, platformYear);
    }
}
