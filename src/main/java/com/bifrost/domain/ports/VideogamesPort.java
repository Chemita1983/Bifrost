package com.bifrost.domain.ports;

import com.bifrost.domain.model.Videogame;
import com.bifrost.domain.model.VideogameQuery;

import java.util.List;

public interface VideogamesPort {

    List<Videogame> getAllVideogames();

    List<Videogame> getVideogameByName(String name);

    Videogame addVideogame(VideogameQuery videogameQuery);
}
