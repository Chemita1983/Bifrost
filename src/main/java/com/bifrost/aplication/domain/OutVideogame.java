package com.bifrost.aplication.domain;

import com.bifrost.aplication.entity.Videogame;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutVideogame {

    private static final String SI = "Si";
    private static final String NO = "No";

    public OutVideogame(Videogame videogame) {
        this.nombre = videogame.getVideogameName();
        this.tipo = videogame.getVideogameType();
        this.anio = videogame.getVideogameYear();
        this.compania = videogame.getCompanyName();
        this.plataforma = videogame.getPlatformName();
        this.digital = videogame.getIsDigital() != 0 ? SI : NO;
        this.completado = videogame.getIsCompleted() != 0 ? SI : NO;
        this.platineado = videogame.getIsPlatinum()  != 0 ? SI: NO;
    }

    @JsonProperty("Nombre del Videojuego")
    private String nombre;

    @JsonProperty("Tipo de Videojuego")
    private String tipo;

    @JsonProperty("Año del Videojuego")
    private Integer anio;

    @JsonProperty("Plataforma")
    private String plataforma;

    @JsonProperty("Compañia")
    private String compania;

    @JsonProperty("Digital")
    private String digital;

    @JsonProperty("Te lo has pasado")
    private String completado;

    @JsonProperty("Platineado")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platineado;
}

