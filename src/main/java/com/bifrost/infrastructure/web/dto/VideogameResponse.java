package com.bifrost.infrastructure.web.dto;

import com.bifrost.domain.Videogame;
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
        Integer hoursPlayed) {

    public static VideogameResponse from(Videogame videogame) {
        return new VideogameResponse(videogame.getId(), videogame.getName(), videogame.getType(),
                videogame.getYear(), videogame.getPlatform() == null ? null
                : PlatformResponse.from(videogame.getPlatform()),
                videogame.getDigitalPlatform() == null ? null
                        : DigitalPlatformResponse.from(videogame.getDigitalPlatform()),
                videogame.getCompanyName(), videogame.getDigital() == 1, videogame.getCompleted() == 1,
                videogame.getPlatinum() == 1, videogame.isMultiplayer(), videogame.getHoursPlayed());
    }
}
