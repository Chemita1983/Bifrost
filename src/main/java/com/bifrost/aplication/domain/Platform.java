package com.bifrost.aplication.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "platform")

public @Data class Platform implements Serializable {

    @Id
    @Column(name = "pk_platform")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer pkPlatform;

    @Column(name="platform_name")
    private String platformName;

    @ManyToOne
    private Videogame videogamePlatform;

    @Column(name="platform_company")
    private String platformCompany;

    @Column(name="platform_year")
    private int platformYear;

}
