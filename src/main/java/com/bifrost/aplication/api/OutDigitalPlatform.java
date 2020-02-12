package com.bifrost.aplication.api;

import com.bifrost.aplication.domain.DigitalPlatform;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutDigitalPlatform {

    public OutDigitalPlatform(DigitalPlatform digitalPlatform){

        this.digitalPlatformName = digitalPlatform.getDigitalPlatformName();
        this.digitalPlatformCompany = digitalPlatform.getDigitalPlatformCompany();
    }

    @JsonProperty("Nombre de la plataforma")
    private String digitalPlatformName;

    @JsonProperty("Empresa de la plataforma")
    private String digitalPlatformCompany;
}
