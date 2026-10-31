package com.bifrost.application.port.out;

import com.bifrost.domain.Videogame;

import java.util.List;
import java.util.Optional;

public interface VideogameRepositoryPort {

    List<Videogame> findAll();

    List<Videogame> findByNameContaining(String name);

    Videogame save(Videogame videogame);
    Optional<Videogame> update(Integer id, Videogame videogame);
}
