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

    private int is_completed;

    private int is_platinum;


    public String getVideogame_name() {
        return videogame_name;
    }

    public void setVideogame_name(String videogame_name) {
        this.videogame_name = videogame_name;
    }

    public String getVideogame_type() {
        return videogame_type;
    }

    public void setVideogame_type(String videogame_type) {
        this.videogame_type = videogame_type;
    }

    public int getVideogame_year() {
        return videogame_year;
    }

    public void setVideogame_year(int videogame_year) {
        this.videogame_year = videogame_year;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(int is_completed) {
        this.is_completed = is_completed;
    }

    public int getIs_platinum() {
        return is_platinum;
    }

    public void setIs_platinum(int is_platinum) {
        this.is_platinum = is_platinum;
    }
}
