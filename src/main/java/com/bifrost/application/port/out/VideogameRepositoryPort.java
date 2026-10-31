package com.bifrost.application.port.out;

import com.bifrost.domain.Videogame;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface VideogameRepositoryPort {

    List<Videogame> findAll();

    List<Videogame> findFiltered(Integer platformId, Boolean isDigital, Boolean isCompleted, Boolean multiplayer, String type);

    List<Videogame> findByNameContaining(String name);

    Videogame save(Videogame videogame);
    Optional<Videogame> update(Integer id, UnaryOperator<Videogame> patch);
    boolean delete(Integer id);
}
