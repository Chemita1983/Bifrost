package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.Videogame;
import com.bifrost.aplication.repository.VideogamesRepository;
import com.bifrost.aplication.service.BifrostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideogameServiceImpl implements BifrostService<Videogame> {

    @Autowired
    private VideogamesRepository videogamesRepository;

    @Override
    public List<Videogame> readAll() {
        return videogamesRepository.getVideogames();
    }

    @Override
    public List<Videogame> readByName(String name) {
        return videogamesRepository.getVideogameByName(name);
    }

    @Override
    public Videogame add(Videogame videogame) {
        return videogamesRepository.save(videogame);
    }

}
