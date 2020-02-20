package com.bifrost.aplication.api;

import com.bifrost.aplication.domain.Videogame;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

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
        this.platinum = verifyPlatinum(videogame.getPlatform().getPlatformName(), videogame.getIsPlatinum());
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


    private String verifyPlatinum(String platformName, Integer isPlatinum) {

        return Optional.ofNullable(platformName)
                .filter(platformNameResult -> platformNameResult.equals("PlayStation 4"))
                .map(result -> isPlatinum != 0 ? SI : NO)
                .orElse(null);
    }
}

