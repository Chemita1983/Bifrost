package com.bifrost.aplication.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "videogames")

public class Videogames {

    @Id
    @Column(name="cod_videogame")
    @GeneratedValue
    private int cod_videogame;

    private String game_name;

    private String digital;

    private String game_company;

    private String game_plataform;

    private String game_type;

    private String game_year;

    private boolean is_completed;

    private boolean platinum;
}
