package com.bifrost.aplication.api;

import com.bifrost.aplication.domain.Platform;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutPlatform {

    public OutPlatform(Platform plataform){
        this.platformName = plataform.getPlatformName();
        this.platformCompany = plataform.getPlatformCompany();
        this.platformYear = plataform.getPlatformYear();
    }

    @JsonProperty("Nombre de la plataforma")
    private String platformName;

    @JsonProperty("Empresa de la plataforma")
    private String platformCompany;

    @JsonProperty("Año de la plataforma")
    private Integer platformYear;
}
