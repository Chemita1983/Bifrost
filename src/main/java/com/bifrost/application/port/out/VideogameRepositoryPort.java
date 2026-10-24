package com.bifrost.application.port.out;

import com.bifrost.domain.Videogame;

import java.util.List;

public interface VideogameRepositoryPort {

    List<Videogame> findAll();

    List<Videogame> findByNameContaining(String name);

    Videogame save(Videogame videogame);
}
