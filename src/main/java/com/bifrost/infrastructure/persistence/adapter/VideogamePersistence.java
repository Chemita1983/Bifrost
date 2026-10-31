package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.application.port.out.VideogameRepositoryPort;
import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
import com.bifrost.infrastructure.persistence.repository.PlatformJpa;
import com.bifrost.infrastructure.persistence.repository.VideogamesJpa;
import org.springframework.stereotype.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Repository
public class VideogamePersistence implements VideogameRepositoryPort {

    private final VideogamesJpa videogamesRepository;
    private final PlatformJpa platformRepository;
    private final DigitalPlatformJpa digitalPlatformJpa;
    private final PersistenceMapper mapper;

    public VideogamePersistence(VideogamesJpa videogamesRepository,
                                       PlatformJpa platformRepository,
                                       DigitalPlatformJpa digitalPlatformJpa,
                                       PersistenceMapper mapper) {
        this.videogamesRepository = videogamesRepository;
        this.platformRepository = platformRepository;
        this.digitalPlatformJpa = digitalPlatformJpa;
        this.mapper = mapper;
    }

    @Override
    public List<Videogame> findAll() {
        return videogamesRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Videogame> findFiltered(Integer platformId, Boolean isDigital, Boolean isCompleted, Boolean multiplayer, String type) {
        return videogamesRepository.findFiltered(platformId, isDigital == null ? null : isDigital ? 1 : 0,
                isCompleted == null ? null : isCompleted ? 1 : 0, multiplayer, type).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public List<Videogame> findByNameContaining(String name) {
        return videogamesRepository.findByVideogameNameContaining(name).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public Videogame save(Videogame videogame) {
        validateRequiredRelations(videogame);
        PlatformEntity platform = resolvePlatform(videogame);
        DigitalPlatformEntity digitalPlatform = resolveDigitalPlatform(videogame);
        VideogameEntity entity = mapper.toEntity(videogame);
        entity.setPlatformEntity(platform);
        entity.setDigitalPlatformEntity(digitalPlatform);
        VideogameEntity saved = videogamesRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Videogame> update(Integer id, UnaryOperator<Videogame> patch) {
        return videogamesRepository.findById(id).map(entity -> {
            Videogame videogame = patch.apply(mapper.toDomain(entity));
            validateRequiredRelations(videogame);
            PlatformEntity platform = resolvePlatform(videogame);
            DigitalPlatformEntity digitalPlatform = resolveDigitalPlatform(videogame);
            mapper.updateEntity(videogame, entity, platform, digitalPlatform);
            return mapper.toDomain(entity);
        });
    }
    private void validateRequiredRelations(Videogame videogame) {
        if (videogame.getPlatform() == null || videogame.getPlatform().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The videogame platform must be specified");
        }
        if (videogame.getDigital() == 1 && (videogame.getDigitalPlatform() == null
                || videogame.getDigitalPlatform().getId() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The digital platform must be specified for digital videogames");
        }
    }

    private PlatformEntity resolvePlatform(Videogame videogame) {
        return platformRepository.findById(videogame.getPlatform().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified platform does not exist"));
    }

    private DigitalPlatformEntity resolveDigitalPlatform(Videogame videogame) {
        if (videogame.getDigitalPlatform() == null) return null;
        Integer id = videogame.getDigitalPlatform().getId();
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digital platform does not exist");
        }
        return digitalPlatformJpa.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digital platform does not exist"));
    }

    @Override
    public boolean delete(Integer id) {
        return videogamesRepository.findById(id).map(entity -> {
            videogamesRepository.delete(entity);
            return true;
        }).orElse(false);
    }
}
