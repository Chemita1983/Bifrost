package com.bifrost.aplication.mappers;

import com.bifrost.aplication.api.OutVideogame;
import com.bifrost.aplication.domain.Videogame;
import com.bifrost.aplication.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VideogameBuilder {

    public List<OutVideogame> convertListConsoleVideogame(List<Videogame> videogames) {
        List<OutVideogame> outVideogames = Optional.of(videogames)
                .filter(videogamesList -> !videogamesList.isEmpty())
                .map(videogamesList -> videogamesList.stream()
                        .map(OutVideogame::new)
                        .collect(Collectors.toList()))
                .orElseThrow(NotFoundException::new);

        return outVideogames.stream()
                .sorted(Comparator.comparing(OutVideogame::getVideogameName))
                .collect(Collectors.toList());
    }
}
