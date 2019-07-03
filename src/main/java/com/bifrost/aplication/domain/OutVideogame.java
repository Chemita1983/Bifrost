package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutVideogame {

    public static final String SI = "Si";
    public static final String NO = "No";

    public OutVideogame(Videogame videogame) {
        this.nombreVideojuego = videogame.getVideogame_name();
        this.tipoVideojuego = videogame.getVideogame_type();
        this.anioVideojuego = videogame.getVideogame_year();
        this.compania = videogame.getCompany_name();
        this.plataforma = videogame.getPlatform_name();
        this.estaCompletado = videogame.getIs_completed() != 0 ? SI : NO;
        this.estaPlatineado = videogame.getIs_platinum()  != 0 ? SI: NO;
    }

    @JsonProperty("Nombre del Videojuego")
    private String nombreVideojuego;

    @JsonProperty("Tipo de Videojuego")
    private String tipoVideojuego;

    @JsonProperty("Año del Videojuego")
    private int anioVideojuego;

    @JsonProperty("Plataforma")
    private String plataforma;

    @JsonProperty("Compañia")
    private String compania;

    @JsonProperty("Te lo has pasado")
    private String estaCompletado;

    @JsonProperty("Platineado")
    private String estaPlatineado;


}
