package com.bifrost.application.port.out;

import com.bifrost.domain.Videogame;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface VideogameRepositoryPort {

    /** Null filters are ignored, so passing only nulls returns every videogame. */
    List<Videogame> findFiltered(Integer platformId, Boolean isDigital, Boolean isCompleted, Boolean multiplayer,
                                 String type);

    List<Videogame> findByNameContaining(String name);

    /** Videogames imported from Steam, that is, with a Steam application ID. */
    List<Videogame> findSteamGames();

    Videogame save(Videogame videogame);

    Optional<Videogame> update(Integer id, UnaryOperator<Videogame> patch);

    /** Updates only the hours played; every other field is left untouched. Returns whether the game exists. */
    boolean updateHoursPlayed(Integer id, int hoursPlayed);

    boolean delete(Integer id);
}
