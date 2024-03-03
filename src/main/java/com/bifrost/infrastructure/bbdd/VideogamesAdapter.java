package com.bifrost.infrastructure.bbdd;

import com.bifrost.domain.model.Videogame;
import com.bifrost.domain.model.VideogameQuery;
import com.bifrost.domain.ports.VideogamesPort;
import com.bifrost.infrastructure.bbdd.entity.VideogameEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideogamesAdapter implements VideogamesPort {

    private final VideogamesRepository videogamesRepository;

    public VideogamesAdapter(VideogamesRepository videogamesRepository) {
        this.videogamesRepository = videogamesRepository;
    }

    @Override
    public List<Videogame> getAllVideogames() {
        List<VideogameEntity> videogames = videogamesRepository.getVideogames();
        return null;
    }

    @Override
    public List<Videogame> getVideogameByName(String name) {
        List<VideogameEntity> videogameByName = videogamesRepository.getVideogameByName(name);
        return null;
    }

    @Override
    public Videogame addVideogame(VideogameQuery videogameQuery) {
        videogamesRepository.save(null);
        return null;
    }
}
