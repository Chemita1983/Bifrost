package com.bifrost.application.port.in;

import com.bifrost.domain.Videogame;

import java.util.List;

public interface VideogameUseCase {

    List<Videogame> getAll();

    List<Videogame> getByName(String name);

    Videogame create(Videogame videogame);
}
