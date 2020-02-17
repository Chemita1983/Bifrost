package com.bifrost.aplication.domain;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "videogames")

public @Data class Videogame implements Serializable {
    @Id
    @Column(name = "id_videogame")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idVideogame;

    private String videogameName;

    private String videogameType;

    private Integer videogameYear;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="platform_videogame")
    private Platform platform;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="digital_platform_videogame")
    private DigitalPlatform digitalPlatform;

    private String companyName;

    private Integer isDigital;

    private Integer isCompleted;

    private Integer isPlatinum;

}
