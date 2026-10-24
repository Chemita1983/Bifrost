package com.bifrost.domain;

public class Platform {

    private Integer id;
    private String name;
    private String company;
    private String year;

    public Platform() {
    }

    public Platform(Integer id, String name, String company, String year) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.year = year;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
