package com.bifrost.aplication.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "digital_platform")

public @Data
class DigitalPlatform {

    @Id
    @Column(name = "pk_digital_platform")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer pkDigitalPlatform;

    @ManyToOne
    private Videogame videogameDigitalPlatform;

    @Column(name="digital_platform_name")
    private String digitalPlatformName;

    @Column(name="digital_platform_company")
    private String digitalPlatformCompany;


}
