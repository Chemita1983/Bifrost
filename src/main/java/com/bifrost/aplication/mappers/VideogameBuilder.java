package com.bifrost.aplication.mappers;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.entity.Videogame;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VideogameBuilder {

    public List<OutVideogame> convertListConsoleVideogame(List<Videogame> videogames) {
        return Optional.of(videogames)
                .map(v -> v.stream()
                        .map(OutVideogame::new)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());

    }
}
