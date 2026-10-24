package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.DigitalPlatform;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record DigitalPlatformResponse(@JsonIgnore Integer id, String digitalPlatformName,
                                      String digitalPlatformCompany) {

    public static DigitalPlatformResponse from(DigitalPlatform platform) {
        return new DigitalPlatformResponse(platform.getId(), platform.getName(), platform.getCompany());
    }
}
