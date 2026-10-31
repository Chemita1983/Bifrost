package com.bifrost.application.service;

import com.bifrost.application.port.in.VideogameUseCase;
import com.bifrost.application.port.out.VideogameRepositoryPort;
import com.bifrost.domain.Videogame;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Service
public class VideogameApplicationService implements VideogameUseCase {

    private final VideogameRepositoryPort videogameRepository;

    public VideogameApplicationService(VideogameRepositoryPort videogameRepository) {
        this.videogameRepository = videogameRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Videogame> getAll() {
        return videogameRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Videogame> getFiltered(Integer platformId, Boolean isDigital, Boolean isCompleted, Boolean multiplayer, String type) {
        return videogameRepository.findFiltered(platformId, isDigital, isCompleted, multiplayer, type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Videogame> getByName(String name) {
        return videogameRepository.findByNameContaining(name);
    }

    @Override
    @Transactional
    public Videogame create(Videogame videogame) {
        return videogameRepository.save(videogame);
    }
    @Override
    @Transactional
    public Optional<Videogame> update(Integer id, UnaryOperator<Videogame> patch) {
        return videogameRepository.update(id, patch);
    }
    @Override
    @Transactional
    public boolean delete(Integer id) {
        return videogameRepository.delete(id);
    }
}
