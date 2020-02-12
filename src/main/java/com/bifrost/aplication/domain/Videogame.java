package com.bifrost.aplication.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private String platformName;

    private String digitalPlatformName;

    private String companyName;

    private Integer isDigital;

    private Integer isCompleted;

    private Integer isPlatinum;

}
