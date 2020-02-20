package com.bifrost.aplication.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "platform")

public @Data class Platform implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_platform")
    private Integer pkPlatform;

    @Column(name="platform_name")
    private String platformName;

    @Column(name="platform_company")
    private String platformCompany;

    @Column(name="platform_year")
    private int platformYear;

}
