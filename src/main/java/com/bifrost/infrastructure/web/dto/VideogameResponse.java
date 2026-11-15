package com.bifrost.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VideogameResponse(
        Integer id,
        String videogameName,
        String videogameType,
        String videogameYear,
        PlatformResponse platform,
        DigitalPlatformResponse digitalPlatform,
        String companyName,
        boolean isDigital,
        boolean isCompleted,
        boolean isPlatinum,
        boolean multiplayer,
        Integer hoursPlayed,
        Integer steamAppId,
        String coverUrl) {
}
