package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.Platform;

public record PlatformResponse(Integer id, String platformName, String platformCompany,
                               String platformYear) {

    public static PlatformResponse from(Platform platform) {
        return new PlatformResponse(platform.getId(), platform.getName(),
                platform.getCompany(), platform.getYear());
    }
}
