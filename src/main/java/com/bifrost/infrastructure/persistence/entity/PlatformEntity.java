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
@Table(name = "platform")
public class PlatformEntity extends BifrostEntity {

    @Column(name = "platform_name")
    private String platformName;

    @Column(name = "platform_company")
    private String platformCompany;

    @Column(name = "platform_year")
    private String platformYear;

}
