package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "digital_platform")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalPlatformEntity extends BifrostEntity {

    @Column(name = "digital_platform_name")
    private String digitalPlatformName;

    @Column(name = "digital_platform_company")
    private String digitalPlatformCompany;

}
