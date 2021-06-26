package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "digital_platform")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalPlatform extends BifrostEntity {

    @Column(name = "digital_platform_name")
    private String digitalPlatformName;

    @Column(name = "digital_platform_company")
    private String digitalPlatformCompany;

}
