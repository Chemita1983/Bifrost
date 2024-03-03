package com.bifrost.infrastructure.api;

import com.bifrost.domain.model.Videogame;
import com.bifrost.domain.model.VideogameQuery;
import com.bifrost.domain.ports.VideogameManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VideogamesController {

    private static final String PATH_VIDEOGAME = "videogame";
    private static final String PATH_VIDEOGAMES = "videogames";

    @Autowired
    private VideogameManagement videogameUseCase;

    @GetMapping(value = PATH_VIDEOGAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Videogame> getVideogameByName(@RequestParam("name") String name) {
        return videogameUseCase.getVideogameByName(name);
    }

    @GetMapping(value = PATH_VIDEOGAMES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Videogame> getVideogames() {
        return videogameUseCase.getAllVideogames();
    }

    @PostMapping(value = PATH_VIDEOGAME, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Videogame addVideogame(@RequestBody VideogameQuery videogameQuery) {
        return videogameUseCase.addVideogame(videogameQuery);
    }
}
