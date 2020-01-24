package com.bifrost.aplication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "platform")

public class Platform {

    @Id
    @Column(name="platform_name")
    private String platform_name;

    private String platform_company;

    private int platform_year;

}
