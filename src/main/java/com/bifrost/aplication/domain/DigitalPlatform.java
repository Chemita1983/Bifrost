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
@Table(name = "digital_platform")

public @Data class DigitalPlatform implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_digital_platform")
    private Integer pkDigitalPlatform;

    @Column(name="digital_platform_name")
    private String digitalPlatformName;

    @Column(name="digital_platform_company")
    private String digitalPlatformCompany;
}
