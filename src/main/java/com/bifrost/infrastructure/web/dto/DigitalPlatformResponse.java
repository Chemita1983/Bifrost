package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.DigitalPlatform;

public record DigitalPlatformResponse(Integer id, String digitalPlatformName,
                                      String digitalPlatformCompany) {

    public static DigitalPlatformResponse from(DigitalPlatform platform) {
        return new DigitalPlatformResponse(platform.getId(), platform.getName(), platform.getCompany());
    }
}
