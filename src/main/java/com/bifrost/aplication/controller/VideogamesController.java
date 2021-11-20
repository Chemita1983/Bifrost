package com.bifrost.aplication.controller;

import com.bifrost.aplication.annotations.ValidVideogame;
import com.bifrost.aplication.domain.Videogame;
import com.bifrost.aplication.service.impl.VideogameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class VideogamesController {

    private static final String PATH = "videogame";

    @Autowired
    private VideogameServiceImpl videogameService;

    @GetMapping(value = PATH + "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Videogame> getVideogameByName(@PathVariable("name") String name) {
        return videogameService.readByName(name);
    }

    @GetMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Videogame> getVideogames() {
        return videogameService.readAll();
    }

    @PostMapping(value = PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Videogame addVideogame(@RequestBody @ValidVideogame Videogame videogame) {
        return videogameService.add(videogame);
    }
}
