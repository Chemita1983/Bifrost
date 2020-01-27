package com.bifrost.aplication.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "videogames")


public @Data class Videogame {
    @Id
    @Column(name = "id_videogame")
    @GeneratedValue
    private Integer idVideogame;

    private String videogameName;

    private String videogameType;

    private Integer videogameYear;

    private String platformName;

    private String companyName;

    private Integer isDigital;

    private Integer isCompleted;

    private Integer isPlatinum;

}
