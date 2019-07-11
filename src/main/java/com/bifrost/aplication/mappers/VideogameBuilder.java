package com.bifrost.aplication.mappers;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.domain.VideogameConsole;
import com.bifrost.aplication.domain.VideogamePc;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VideogameBuilder {

    public OutVideogame convertConsoleVideogame(Optional<VideogameConsole> videogame) {
        return new OutVideogame(videogame.orElse(new VideogameConsole()));
    }

    public List<OutVideogame> convertListConsoleVideogame(List<VideogameConsole> videogames) {
        return Optional.of(videogames)
                .map(v -> v.stream()
                        .map(OutVideogame::new)
                        .collect(Collectors.toList())).orElse(new ArrayList<>());

    }

    public OutVideogame convertPcVideogame(Optional<VideogamePc> videogame) {
        return new OutVideogame(videogame.orElse(new VideogamePc()));
    }

    public List<OutVideogame> convertListPcVideogames(List<VideogamePc> videogames) {
        return Optional.of(videogames)
                .map(v -> v.stream()
                        .map(OutVideogame::new)
                        .collect(Collectors.toList())).orElse(new ArrayList<>());

    }
}
