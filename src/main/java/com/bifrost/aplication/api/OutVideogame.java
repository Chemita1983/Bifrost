package com.bifrost.aplication.api;

import com.bifrost.aplication.domain.Platform;
import com.bifrost.aplication.domain.Videogame;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class OutVideogame {

    private static final String SI = "Si";
    private static final String NO = "No";

    public OutVideogame(Videogame videogame) {
        this.videogameName = videogame.getVideogameName();
        this.videogameType = videogame.getVideogameType();
        this.videogameYear = videogame.getVideogameYear();
        this.videogameCompany = videogame.getCompanyName();
        this.videogamePlatform = videogame.getPlatform().getPlatformName();
        this.digitalPlatform = Optional.ofNullable(videogame.getDigitalPlatform())
                .map(digitalPlatform -> digitalPlatform.getDigitalPlatformName()).orElse(null);
        this.digital = videogame.getIsDigital() != 0 ? SI : NO;
        this.completed = videogame.getIsCompleted() != 0 ? SI : NO;
        this.platinum = verifyPlatinum(videogame.getPlatform(), videogame.getIsPlatinum());
        this.players = videogame.getPlayers();
    }

    @JsonProperty("Nombre del Videojuego")
    private String videogameName;

    @JsonProperty("Tipo de Videojuego")
    private String videogameType;

    @JsonProperty("Año del Videojuego")
    private Integer videogameYear;

    @JsonProperty("Plataforma")
    private String videogamePlatform;

    @JsonProperty("Compañia")
    private String videogameCompany;

    @JsonProperty("Jugadores")
    private Integer players;

    @JsonProperty("Digital")
    private String digital;

    @JsonProperty("Plataforma Digital")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String digitalPlatform;

    @JsonProperty("Te lo has pasado")
    private String completed;

    @JsonProperty("Platineado")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platinum;


    private String verifyPlatinum(Platform platform, Integer isPlatinum) {

        return Optional.ofNullable(platform)
                .filter(platformNameResult -> platformNameResult.getPkPlatform().equals(4))
                .map(result -> isPlatinum != 0 ? SI : NO)
                .orElse(null);
    }
}

