package com.bifrost.aplication.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "digital_platform")

public @Data
class DigitalPlatform {

    @Id
    @Column(name="digital_platform_name")
    private String digitalPlatformName;

    @Column(name="digital_platform_company")
    private String digitalPlatformCompany;
}
