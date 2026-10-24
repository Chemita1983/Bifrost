package com.bifrost.infrastructure.web.dto;

public record VideogameRequest(
        String videogameName,
        String videogameType,
        String videogameYear,
        Integer platformId,
        Integer digitalPlatformId,
        String companyName,
        int isDigital,
        int isCompleted,
        int isPlatinum,
        boolean multiplayer) {
}
