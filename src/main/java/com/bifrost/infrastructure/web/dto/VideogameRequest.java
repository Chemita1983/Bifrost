package com.bifrost.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record VideogameRequest(
        @NotBlank String videogameName,
        String videogameType,
        String videogameYear,
        @Positive Integer platformId,
        @Positive Integer digitalPlatformId,
        String companyName,
        @Min(0) @Max(1) int isDigital,
        @Min(0) @Max(1) int isCompleted,
        @Min(0) @Max(1) int isPlatinum,
        boolean multiplayer) {
}
