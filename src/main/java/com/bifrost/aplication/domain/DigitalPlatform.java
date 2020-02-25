package com.bifrost.aplication.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "digital_platform")

public class DigitalPlatform implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_digital_platform")
    private Integer pkDigitalPlatform;

    @Column(name="digital_platform_name")
    private String digitalPlatformName;

    @Column(name="digital_platform_company")
    private String digitalPlatformCompany;
}
