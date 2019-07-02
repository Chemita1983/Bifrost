package com.bifrost.aplication.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "videogame")

public class Videogames {

    @Id
    @Column(name="id_videogame")
    @GeneratedValue
    private int id_videogame;

    private String videogame_name;

    private String videogame_type;

    private int videogame_year;

    private String platform_name;

    private String company_name;

    private int is_completed;

    private int is_platinum;

}
