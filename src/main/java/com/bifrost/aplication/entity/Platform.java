package com.bifrost.aplication.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "platform")

public @Data class Platform {

    @Id
    @Column(name="platform_name")
    private String platformName;

    @Column(name="platform_company")
    private String platformCompany;

    @Column(name="platform_year")
    private int platformYear;

}
