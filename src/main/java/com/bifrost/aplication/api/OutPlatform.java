package com.bifrost.aplication.api;

import com.bifrost.aplication.domain.Platform;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutPlatform {

    public OutPlatform(Platform platform){
        this.id = platform.getPkPlatform();
        this.platformName = platform.getPlatformName();
        this.platformCompany = platform.getPlatformCompany();
        this.platformYear = platform.getPlatformYear();
    }

    @JsonProperty("Id")
    private Integer id;

    @JsonProperty("Nombre de la plataforma")
    private String platformName;

    @JsonProperty("Empresa de la plataforma")
    private String platformCompany;

    @JsonProperty("Año de la plataforma")
    private Integer platformYear;
}
