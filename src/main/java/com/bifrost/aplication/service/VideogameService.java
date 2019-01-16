package com.bifrost.aplication.service;

import com.bifrost.aplication.domain.Videogames;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VideogameService {

    List<Videogames> getAllVideogames();

    Optional<Videogames> getVideogameByName(String gameName);

}
