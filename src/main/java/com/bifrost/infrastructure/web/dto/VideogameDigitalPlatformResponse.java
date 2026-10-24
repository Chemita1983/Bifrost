package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.DigitalPlatform;

public record VideogameDigitalPlatformResponse(String digitalPlatformName,
                                                String digitalPlatformCompany) {

    public static VideogameDigitalPlatformResponse from(DigitalPlatform platform) {
        return new VideogameDigitalPlatformResponse(platform.getName(), platform.getCompany());
    }
}
