package com.bifrost.application.port.in;

import com.bifrost.domain.Videogame;

import java.util.List;
import java.util.Optional;

public interface VideogameUseCase {

    List<Videogame> getAll();

    List<Videogame> getByName(String name);

    Videogame create(Videogame videogame);
    Optional<Videogame> update(Integer id, Videogame videogame);
}
