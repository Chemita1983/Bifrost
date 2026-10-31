package com.bifrost.application.port.in;

import com.bifrost.domain.Videogame;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface VideogameUseCase {

    List<Videogame> getAll();

    List<Videogame> getFiltered(Integer platformId, Boolean isDigital, Boolean isCompleted, Boolean multiplayer, String type);

    List<Videogame> getByName(String name);

    Videogame create(Videogame videogame);
    Optional<Videogame> update(Integer id, UnaryOperator<Videogame> patch);
    boolean delete(Integer id);
}
