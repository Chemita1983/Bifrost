package com.bifrost.domain.model;

import com.bifrost.domain.exceptions.NotFoundException;

import java.util.stream.Stream;

public enum Platforms {

    NINTENDO_SWITCH(1),
    NINTENDO_3DS(2),
    PC(3),
    PLAYSTATION_4(4),
    PLAYSTATION_5(5),
    XBOX(6);

    private final Integer id;

    Platforms(Integer id) {
        this.id = id;
    }

    public Integer getPlatformId(String platform){
        return Stream.of(Platforms.values())
                .filter(p -> p.name().equalsIgnoreCase(platform))
                .findFirst()
                .map(p-> p.id)
                .orElseThrow(() -> new NotFoundException("Platform not recognized"));
    }
}
