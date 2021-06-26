package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videogames")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Videogame extends BifrostEntity {

    private String videogameName;

    private String videogameType;

    private String videogameYear;

    @OneToOne
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @OneToOne
    @JoinColumn(name = "digital_platform_id")
    private DigitalPlatform digitalPlatform;

    private String companyName;

    private int isDigital;

    private int isCompleted;

    private int isPlatinum;

    private int players;

}
