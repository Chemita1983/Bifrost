package com.bifrost.infrastructure.bbdd.entity;

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
public class VideogameEntity extends BifrostEntity {

    private String name;

    private String type;

    private String year;

    @OneToOne
    @JoinColumn(name = "platform_id")
    private PlatformEntity platform;

    @OneToOne
    @JoinColumn(name = "digital_platform_id")
    private DigitalPlatformEntity digitalPlatform;

    private String company;

    private int isDigital;

    private int isCompleted;

    private int isPlatinum;

    private int players;

}
