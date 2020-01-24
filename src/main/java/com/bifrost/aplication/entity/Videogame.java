package com.bifrost.aplication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "videogames_console")

public class VideogameConsole {
    @Id
    @Column(name = "id_videogame")
    @GeneratedValue
    private Integer id_videogame;

    private String videogame_name;

    private String videogame_type;

    private Integer videogame_year;

    private String platform_name;

    private String company_name;

    private Integer is_digital;

    private Integer is_completed;

    private Integer is_platinum;

    private Integer pretended;

    private Integer psplus;

    private Integer gameswithgold;

    public String getVideogame_name() {
        return videogame_name;
    }

    public String getVideogame_type() {
        return videogame_type;
    }

    public Integer getVideogame_year() {
        return videogame_year;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public Integer getIs_digital() {
        return is_digital;
    }

    public Integer getIs_completed() {
        return is_completed;
    }

    public Integer getIs_platinum() {
        return is_platinum;
    }

    public Integer getPretended() {
        return pretended;
    }

    public void setPretended(Integer pretended) {
        this.pretended = pretended;
    }

    public Integer getPsplus() {
        return psplus;
    }

    public void setPsplus(Integer psplus) {
        this.psplus = psplus;
    }

    public Integer getGameswithgold() {
            return gameswithgold;
    }

    public void setGameswithgold(Integer gameswithgold) {
        this.gameswithgold = gameswithgold;
    }
}
