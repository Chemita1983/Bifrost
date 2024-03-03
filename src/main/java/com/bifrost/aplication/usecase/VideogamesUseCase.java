package com.bifrost.aplication.usecase;

import com.bifrost.domain.model.Videogame;
import com.bifrost.domain.model.VideogameQuery;
import com.bifrost.domain.ports.VideogameManagement;
import com.bifrost.domain.ports.VideogamesPort;

import java.util.List;

public class VideogamesUseCase implements VideogameManagement {

    private final VideogamesPort videogamesPort;

    public VideogamesUseCase(VideogamesPort videogamesPort){
        this.videogamesPort = videogamesPort;
    }
    @Override
    public List<Videogame> getAllVideogames() {
        return videogamesPort.getAllVideogames();
    }

    @Override
    public List<Videogame> getVideogameByName(String name) {
        return videogamesPort.getVideogameByName(name);
    }

    @Override
    public Videogame addVideogame(VideogameQuery videogameQuery) {
        return videogamesPort.addVideogame(videogameQuery);
    }
}
