package com.bifrost.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Videogame {

    private String name;

    private String type;

    private String year;

    private Platform platform;

    private DigitalPlatform digitalPlatform;

    private String company;

    private int isDigital;

    private int isCompleted;

    private int isPlatinum;

    private int players;
}
