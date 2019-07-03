package com.bifrost.aplication.domain;

import javax.persistence.*;

@Entity
@Table(name = "videogame")

public class Videogame {
    @Id
    @Column(name="id_videogame")
    @GeneratedValue
    private int id_videogame;

    private String videogame_name;

    private String videogame_type;

    private int videogame_year;

    private String platform_name;

    private String company_name;

    private int is_digital;

    private int is_completed;

    private int is_platinum;


    public String getVideogame_name() {
        return videogame_name;
    }


    public String getVideogame_type() {
        return videogame_type;
    }


    public int getVideogame_year() {
        return videogame_year;
    }


    public String getPlatform_name() { return platform_name;    }


    public String getCompany_name() {
        return company_name;
    }


    public int getIs_digital() { return is_digital; }

    public int getIs_completed() {
        return is_completed;
    }


    public int getIs_platinum() {
        return is_platinum;
    }

}
