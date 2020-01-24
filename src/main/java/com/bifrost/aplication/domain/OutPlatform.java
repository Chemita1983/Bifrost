package com.bifrost.aplication.domain;

import com.bifrost.aplication.entity.Platform;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutPlatform {

    public OutPlatform(Platform plataform){
        this.nombrePlataforma = plataform.getPlatformName();
        this.anioPlataforma = plataform.getPlatformYear();
        this.companiaPlataforma = plataform.getPlatformCompany();
    }

    @JsonProperty("Nombre de la plataforma")
    private String nombrePlataforma;

    @JsonProperty("Empresa de la plataforma")
    private String companiaPlataforma;

    @JsonProperty("Año de la plataforma")
    private Integer anioPlataforma;
}
