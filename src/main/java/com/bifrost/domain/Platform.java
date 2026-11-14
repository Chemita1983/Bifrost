package com.bifrost.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Platform {

    private Integer id;
    private String name;
    private String company;
    private String year;
}
