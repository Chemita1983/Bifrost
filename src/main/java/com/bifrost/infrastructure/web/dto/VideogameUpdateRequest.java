package com.bifrost.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;

public record VideogameUpdateRequest(
        @JsonProperty(value = "videogameName", required = true) @JsonDeserialize(using = StrictValues.Text.class) String videogameName,
        @JsonProperty(value = "videogameType", required = true) @JsonDeserialize(using = StrictValues.Text.class) String videogameType,
        @JsonProperty(value = "videogameYear", required = true) @JsonDeserialize(using = StrictValues.Text.class) String videogameYear,
        @JsonProperty(value = "platformId", required = true) @JsonDeserialize(using = StrictValues.IntegerValue.class) Integer platformId,
        @JsonProperty(value = "digitalPlatformId", required = true) @JsonDeserialize(using = StrictValues.IntegerValue.class) Integer digitalPlatformId,
        @JsonProperty(value = "companyName", required = true) @JsonDeserialize(using = StrictValues.Text.class) String companyName,
        @JsonProperty(value = "isDigital", required = true) @JsonDeserialize(using = StrictValues.IntegerValue.class) Integer isDigital,
        @JsonProperty(value = "isCompleted", required = true) @JsonDeserialize(using = StrictValues.IntegerValue.class) Integer isCompleted,
        @JsonProperty(value = "isPlatinum", required = true) @JsonDeserialize(using = StrictValues.IntegerValue.class) Integer isPlatinum,
        @JsonProperty(value = "multiplayer", required = true) @JsonDeserialize(using = StrictValues.BooleanValue.class) Boolean multiplayer) {
    public VideogameUpdateRequest {
        Objects.requireNonNull(videogameName, "videogameName is required");
        Objects.requireNonNull(videogameType, "videogameType is required");
        Objects.requireNonNull(videogameYear, "videogameYear is required");
        Objects.requireNonNull(companyName, "companyName is required");
        Objects.requireNonNull(isDigital, "isDigital is required");
        if (isDigital != 0 && isDigital != 1) throw new IllegalArgumentException("isDigital must be 0 or 1");
        Objects.requireNonNull(isCompleted, "isCompleted is required");
        if (isCompleted != 0 && isCompleted != 1) throw new IllegalArgumentException("isCompleted must be 0 or 1");
        Objects.requireNonNull(isPlatinum, "isPlatinum is required");
        if (isPlatinum != 0 && isPlatinum != 1) throw new IllegalArgumentException("isPlatinum must be 0 or 1");
        Objects.requireNonNull(multiplayer, "multiplayer is required");
    }

    public VideogameRequest toRequest() {
        return new VideogameRequest(videogameName, videogameType, videogameYear, platformId, digitalPlatformId, companyName, isDigital, isCompleted, isPlatinum, multiplayer);
    }
}
