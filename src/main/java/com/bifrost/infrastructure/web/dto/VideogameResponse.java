package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.Videogame;

public record VideogameResponse(
        Integer id,
        String videogameName,
        String videogameType,
        String videogameYear,
        PlatformResponse platform,
        DigitalPlatformResponse digitalPlatform,
        String companyName,
        int isDigital,
        int isCompleted,
        int isPlatinum,
        boolean multiplayer) {

    public static VideogameResponse from(Videogame videogame) {
        return new VideogameResponse(videogame.getId(), videogame.getName(), videogame.getType(),
                videogame.getYear(), videogame.getPlatform() == null ? null
                : PlatformResponse.from(videogame.getPlatform()),
                videogame.getDigitalPlatform() == null ? null
                        : DigitalPlatformResponse.from(videogame.getDigitalPlatform()),
                videogame.getCompanyName(), videogame.getDigital(), videogame.getCompleted(),
                videogame.getPlatinum(), videogame.isMultiplayer());
    }
}
