package com.bifrost.aplication.domain;

import com.bifrost.aplication.entity.Videogame;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutVideogame {

    private static final String SI = "Si";
    private static final String NO = "No";

    public OutVideogame(Videogame videogame) {
        this.nombre = videogame.getVideogame_name();
        this.tipo = videogame.getVideogame_type();
        this.anio = videogame.getVideogame_year();
        this.compania = videogame.getCompany_name();
        this.plataforma = videogame.getPlatform_name();
        this.digital = videogame.getIs_digital() != 0 ? SI : NO;
        this.completado = videogame.getIs_completed() != 0 ? SI : NO;
        this.platineado = videogame.getIs_platinum()  != 0 ? SI: NO;
        this.pretendido = videogame.getPretended() != 0 ? SI: null;
        this.psplus = videogame.getPsplus() != 0 ? SI: NO;
        this.gamesWithGold = videogame.getGameswithgold() != 0 ? SI: NO;
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

    @JsonProperty("Pretendido")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pretendido;

    @JsonProperty("Es de PSPlus")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String psplus;

    @JsonProperty("Es de Games With Gold")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gamesWithGold;




}

