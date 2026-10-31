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
    public List<Videogame> findByNameContaining(String name) {
        return videogamesRepository.findByVideogameNameContaining(name).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public Videogame save(Videogame videogame) {
        VideogameEntity entity = mapper.toEntity(videogame);
        if (videogame.getPlatform() != null && videogame.getPlatform().getId() != null) {
            PlatformEntity platform = platformRepository.getReferenceById(videogame.getPlatform().getId());
            entity.setPlatformEntity(platform);
        }
        if (videogame.getDigitalPlatform() != null && videogame.getDigitalPlatform().getId() != null) {
            DigitalPlatformEntity digitalPlatform =
                    digitalPlatformJpa.getReferenceById(videogame.getDigitalPlatform().getId());
            entity.setDigitalPlatformEntity(digitalPlatform);
        }
        VideogameEntity saved = videogamesRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Videogame> update(Integer id, UnaryOperator<Videogame> patch) {
        return videogamesRepository.findById(id).map(entity -> {
            Videogame videogame = patch.apply(mapper.toDomain(entity));
            PlatformEntity platform = videogame.getPlatform() == null ? null
                    : entity.getPlatformEntity() != null && videogame.getPlatform().getId().equals(entity.getPlatformEntity().getId())
                    ? entity.getPlatformEntity() : platformRepository.findById(videogame.getPlatform().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Platform not found"));
            DigitalPlatformEntity digitalPlatform = videogame.getDigitalPlatform() == null ? null
                    : entity.getDigitalPlatformEntity() != null && videogame.getDigitalPlatform().getId().equals(entity.getDigitalPlatformEntity().getId())
                    ? entity.getDigitalPlatformEntity() : digitalPlatformJpa.findById(videogame.getDigitalPlatform().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Digital platform not found"));
            mapper.updateEntity(videogame, entity, platform, digitalPlatform);
            return mapper.toDomain(entity);
        });
    }
}
