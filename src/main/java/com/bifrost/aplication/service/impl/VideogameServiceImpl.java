package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.Videogames;
import com.bifrost.aplication.repository.VideogamesRepository;
import com.bifrost.aplication.service.VideogameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideogameServiceImpl implements VideogameService {

    @Autowired
    private VideogamesRepository videogamesRepository;

    @Override
    public List<Videogames> getAllVideogames() {

        List<Videogames> videogames = videogamesRepository.getVideogames();

        return videogames;
    }

    @Override
    public Optional<Videogames> getVideogameByName(String gameName) {
        return videogamesRepository.findVideogameByName(gameName);
    }
}
