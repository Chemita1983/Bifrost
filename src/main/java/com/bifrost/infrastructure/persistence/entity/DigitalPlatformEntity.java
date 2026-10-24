package com.bifrost.infrastructure.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "digital_platform")
public class DigitalPlatformEntity extends BifrostEntity {

    @Column(name = "digital_platform_name")
    private String digitalPlatformName;

    @Column(name = "digital_platform_company")
    private String digitalPlatformCompany;

}
