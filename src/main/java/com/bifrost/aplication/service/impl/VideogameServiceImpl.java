package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.VideogameEntity;
import com.bifrost.aplication.repository.VideogamesRepository;
import com.bifrost.aplication.service.BifrostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideogameServiceImpl implements BifrostService<VideogameEntity> {

    @Autowired
    private VideogamesRepository videogamesRepository;

    @Override
    public List<VideogameEntity> readAll() {
        return videogamesRepository.getVideogames();
    }

    @Override
    public List<VideogameEntity> readByName(String name) {
        return videogamesRepository.getVideogameByName(name);
    }

    @Override
    public VideogameEntity add(VideogameEntity videogameEntity) {
        return videogamesRepository.save(videogameEntity);
    }
}
