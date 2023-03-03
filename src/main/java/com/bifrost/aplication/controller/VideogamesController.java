package com.bifrost.aplication.controller;

import com.bifrost.aplication.annotations.ValidVideogame;
import com.bifrost.aplication.domain.Videogame;
import com.bifrost.aplication.service.impl.VideogameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class VideogamesController {

    private static final String PATH_VIDEOGAME = "videogame";
    private static final String PATH_VIDEOGAMES = "videogames";

    @Autowired
    private VideogameServiceImpl videogameService;

    @GetMapping(value = PATH_VIDEOGAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Videogame> getVideogameByName(@RequestParam("name") String name) {
        return videogameService.readByName(name);
    }

    @GetMapping(value = PATH_VIDEOGAMES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Videogame> getVideogames() {
        return videogameService.readAll();
    }

    @PostMapping(value = PATH_VIDEOGAME, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Videogame addVideogame(@RequestBody @ValidVideogame Videogame videogame) {
        return videogameService.add(videogame);
    }
}
