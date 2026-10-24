package com.bifrost.domain;

public class Videogame {

    private Integer id;
    private String name;
    private String type;
    private String year;
    private Platform platform;
    private DigitalPlatform digitalPlatform;
    private String companyName;
    private int digital;
    private int completed;
    private int platinum;
    private boolean multiplayer;

    public Videogame() {
    }

    public Videogame(Integer id, String name, String type, String year, Platform platform,
                     DigitalPlatform digitalPlatform, String companyName, int digital,
                      int completed, int platinum, boolean multiplayer) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.year = year;
        this.platform = platform;
        this.digitalPlatform = digitalPlatform;
        this.companyName = companyName;
        this.digital = digital;
        this.completed = completed;
        this.platinum = platinum;
        this.multiplayer = multiplayer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public DigitalPlatform getDigitalPlatform() {
        return digitalPlatform;
    }

    public void setDigitalPlatform(DigitalPlatform digitalPlatform) {
        this.digitalPlatform = digitalPlatform;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getDigital() {
        return digital;
    }

    public void setDigital(int digital) {
        this.digital = digital;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getPlatinum() {
        return platinum;
    }

    public void setPlatinum(int platinum) {
        this.platinum = platinum;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }
}
