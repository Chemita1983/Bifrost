package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videogames")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Videogame extends BifrostEntity implements Serializable {

    @Id
    @Column(name = "id_videogame")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer idVideogame;

    private String videogameName;

    private String videogameType;

    private Integer videogameYear;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="platform_videogame")
    private Platform platform;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="digital_platform_videogame")
    private DigitalPlatform digitalPlatform;

    private String companyName;

    private Integer isDigital;

    private Integer isCompleted;

    private Integer isPlatinum;

    private Integer players;

}
