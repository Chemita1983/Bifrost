package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutVideogame {

    public static final String SI = "Si";
    public static final String NO = "No";

    public OutVideogame(Videogame videogame) {
        this.nombre = videogame.getVideogame_name();
        this.tipo = videogame.getVideogame_type();
        this.anio = videogame.getVideogame_year();
        this.compania = videogame.getCompany_name();
        this.plataforma = videogame.getPlatform_name();
        this.digital = videogame.getIs_digital() != 0 ? SI : NO;
        this.completado = videogame.getIs_completed() != 0 ? SI : NO;
        this.platineado = videogame.getIs_platinum()  != 0 ? SI: NO;
    }

    @JsonProperty("Nombre del Videojuego")
    private String nombre;

    @JsonProperty("Tipo de Videojuego")
    private String tipo;

    @JsonProperty("Año del Videojuego")
    private int anio;

    @JsonProperty("Plataforma")
    private String plataforma;

    @JsonProperty("Compañia")
    private String compania;

    @JsonProperty("Digital")
    private String digital;

    @JsonProperty("Te lo has pasado")
    private String completado;

    @JsonProperty("Platineado")
    private String platineado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public String getCompletado() {
        return completado;
    }

    public void setCompletado(String completado) {
        this.completado = completado;
    }

    public String getPlatineado() {
        return platineado;
    }

    public void setPlatineado(String platineado) {
        this.platineado = platineado;
    }
}

