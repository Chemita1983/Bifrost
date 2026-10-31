package com.bifrost.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record PlatformRequest(@NotBlank String platformName, String platformCompany, String platformYear) {
}
