package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.Platform;

public record VideogamePlatformResponse(Integer id, String platformName, String platformCompany) {

    public static VideogamePlatformResponse from(Platform platform) {
        return new VideogamePlatformResponse(platform.getId(), platform.getName(), platform.getCompany());
    }
}
