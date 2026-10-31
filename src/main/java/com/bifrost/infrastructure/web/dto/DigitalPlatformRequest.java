package com.bifrost.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record DigitalPlatformRequest(@NotBlank String digitalPlatformName, String digitalPlatformCompany) {
}
