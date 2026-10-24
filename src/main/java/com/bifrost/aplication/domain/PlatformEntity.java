package com.bifrost.aplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "platform")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformEntity extends BifrostEntity {

    @Column(name = "platform_name")
    private String platformName;

    @Column(name = "platform_company")
    private String platformCompany;

    @Column(name = "platform_year")
    private String platformYear;

}
