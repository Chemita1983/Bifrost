package com.bifrost.infrastructure.bbdd.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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
public class DigitalPlatformEntity extends BifrostEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "company")
    private String company;
}
