package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videogames")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideogameEntity extends BifrostEntity {

    private String videogameName;

    private String videogameType;

    private String videogameYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private PlatformEntity platformEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "digital_platform_id")
    private DigitalPlatformEntity digitalPlatformEntity;

    private String companyName;

    private int isDigital;

    private int isCompleted;

    private int isPlatinum;

    private int players;

}
