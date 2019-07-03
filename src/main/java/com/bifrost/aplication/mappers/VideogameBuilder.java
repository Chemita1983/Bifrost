package com.bifrost.aplication.mappers;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.domain.Videogame;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VideogameBuilder {

    public OutVideogame convertVideogame(Optional<Videogame> videogame) {
        return new OutVideogame(videogame.orElse(new Videogame()));
    }

    public List<OutVideogame> convertListVideogame(List<Videogame> videogames) {
        return Optional.of(videogames)
                .map(v -> v.stream()
                        .map(OutVideogame::new)
                        .collect(Collectors.toList())).orElse(new ArrayList<>());

    }
}
