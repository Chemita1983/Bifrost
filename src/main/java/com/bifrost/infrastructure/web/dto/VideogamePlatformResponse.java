package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.Platform;

public record VideogamePlatformResponse(String platformName, String platformCompany) {

    public static VideogamePlatformResponse from(Platform platform) {
        return new VideogamePlatformResponse(platform.getName(), platform.getCompany());
    }
}
