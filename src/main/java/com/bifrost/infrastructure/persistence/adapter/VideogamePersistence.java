package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.application.port.out.VideogameRepositoryPort;
import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
import com.bifrost.infrastructure.persistence.repository.PlatformJpa;
import com.bifrost.infrastructure.persistence.repository.VideogamesJpa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Repository
public class VideogamePersistence implements VideogameRepositoryPort {

    private final VideogamesJpa videogamesRepository;
    private final PlatformJpa platformRepository;
    private final DigitalPlatformJpa digitalPlatformRepository;
    private final PersistenceMapper mapper;

    public VideogamePersistence(VideogamesJpa videogamesRepository, PlatformJpa platformRepository,
                                DigitalPlatformJpa digitalPlatformRepository, PersistenceMapper mapper) {
        this.videogamesRepository = videogamesRepository;
        this.platformRepository = platformRepository;
        this.digitalPlatformRepository = digitalPlatformRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Videogame> findFiltered(Integer platformId, Boolean isDigital, Boolean isCompleted,
                                        Boolean multiplayer, String type) {
        return videogamesRepository.findFiltered(platformId, toFlag(isDigital), toFlag(isCompleted), multiplayer, type)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Videogame> findByNameContaining(String name) {
        return videogamesRepository.findByVideogameNameContaining(name).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Videogame save(Videogame videogame) {
        VideogameEntity entity = new VideogameEntity();
        copyWithRelations(videogame, entity);
        return mapper.toDomain(videogamesRepository.save(entity));
    }

    @Override
    public Optional<Videogame> update(Integer id, UnaryOperator<Videogame> patch) {
        return videogamesRepository.findById(id).map(entity -> {
            copyWithRelations(patch.apply(mapper.toDomain(entity)), entity);
            return mapper.toDomain(entity);
        });
    }

    @Override
    public boolean delete(Integer id) {
        return videogamesRepository.findById(id).map(entity -> {
            videogamesRepository.delete(entity);
            return true;
        }).orElse(false);
    }

    /** Validates the relations and copies the videogame into the entity. Nothing is changed if a check fails. */
    private void copyWithRelations(Videogame videogame, VideogameEntity entity) {
        Integer platformId = videogame.getPlatform() == null ? null : videogame.getPlatform().getId();
        Integer digitalPlatformId = videogame.getDigitalPlatform() == null ? null
                : videogame.getDigitalPlatform().getId();

        if (platformId == null) {
            throw badRequest("The videogame platform must be specified");
        }
        if (videogame.getDigital() == 1 && digitalPlatformId == null) {
            throw badRequest("The digital platform must be specified for digital videogames");
        }
        PlatformEntity platform = platformRepository.findById(platformId)
                .orElseThrow(() -> badRequest("The specified platform does not exist"));
        DigitalPlatformEntity digitalPlatform = digitalPlatformId == null ? null
                : digitalPlatformRepository.findById(digitalPlatformId)
                .orElseThrow(() -> badRequest("The specified digital platform does not exist"));

        mapper.updateEntity(videogame, entity, platform, digitalPlatform);
    }

    private static Integer toFlag(Boolean value) {
        return value == null ? null : value ? 1 : 0;
    }

    private static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
