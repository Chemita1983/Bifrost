package com.bifrost.infrastructure.mapper;

import com.bifrost.domain.DigitalPlatform;
import com.bifrost.domain.Platform;
import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.web.dto.DigitalPlatformRequest;
import com.bifrost.infrastructure.web.dto.DigitalPlatformResponse;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import com.bifrost.infrastructure.web.dto.PlatformResponse;
import com.bifrost.infrastructure.web.dto.VideogameRequest;
import com.bifrost.infrastructure.web.dto.VideogameResponse;
import org.springframework.stereotype.Component;

@Component
public class WebMapper {

    public Platform toDomain(PlatformRequest request) {
        return new Platform(null, request.platformName(), request.platformCompany(), request.platformYear());
    }

    public PlatformResponse toResponse(Platform platform) {
        return PlatformResponse.from(platform);
    }

    public DigitalPlatform toDomain(DigitalPlatformRequest request) {
        return new DigitalPlatform(null, request.digitalPlatformName(), request.digitalPlatformCompany());
    }

    public DigitalPlatformResponse toResponse(DigitalPlatform digitalPlatform) {
        return DigitalPlatformResponse.from(digitalPlatform);
    }

    public Videogame toDomain(VideogameRequest request) {
        Platform platform = request.platformId() == null ? null
                : new Platform(request.platformId(), null, null, null);
        DigitalPlatform digitalPlatform = request.digitalPlatformId() == null ? null
                : new DigitalPlatform(request.digitalPlatformId(), null, null);

        return new Videogame(null, request.videogameName(), request.videogameType(),
                request.videogameYear(), platform, digitalPlatform, request.companyName(),
                request.isDigital(), request.isCompleted(), request.isPlatinum(), request.players());
    }

    public VideogameResponse toResponse(Videogame videogame) {
        return VideogameResponse.from(videogame);
    }
}
