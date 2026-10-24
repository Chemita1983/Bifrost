package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.Platform;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record PlatformResponse(@JsonIgnore Integer id, String platformName, String platformCompany,
                               String platformYear) {

    public static PlatformResponse from(Platform platform) {
        return new PlatformResponse(platform.getId(), platform.getName(),
                platform.getCompany(), platform.getYear());
    }
}
