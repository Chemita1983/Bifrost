package com.bifrost.aplication.controller;

import com.bifrost.aplication.domain.Videogames;
import com.bifrost.aplication.service.VideogameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class VideogamesController {

    @Autowired
    private VideogameService videogameService;

    @GetMapping(value="/get" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Videogames> getVideogames() {
        return videogameService.getAllVideogames();
    }

    @GetMapping(value="/{name}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Videogames> getVideogames(@PathVariable("name") String gameName) {
        return videogameService.getVideogameByName(gameName);
    }
}
