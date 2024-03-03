package com.bifrost.infrastructure.bbdd.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "platform")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformEntity extends BifrostEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "company")
    private String company;

    @Column(name = "year")
    private String year;

}
