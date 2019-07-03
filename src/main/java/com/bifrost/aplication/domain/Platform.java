package com.bifrost.aplication.domain;

import javax.persistence.*;

@Entity
@Table(name = "platform")

public class Platform {

    @Id
    @Column(name="platform_name")
    private String platform_name;

    private String platform_company;

    private int platform_year;

}
