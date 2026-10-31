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

import java.util.List;
import java.util.Optional;

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
    public Optional<Videogame> update(Integer id, Videogame videogame) {
        return videogamesRepository.findById(id).map(entity -> {
            entity.setVideogameName(videogame.getName());
            entity.setVideogameType(videogame.getType());
            entity.setVideogameYear(videogame.getYear());
            entity.setCompanyName(videogame.getCompanyName());
            entity.setIsDigital(videogame.getDigital());
            entity.setIsCompleted(videogame.getCompleted());
            entity.setIsPlatinum(videogame.getPlatinum());
            entity.setMultiplayer(videogame.isMultiplayer());
            entity.setPlatformEntity(videogame.getPlatform() == null || videogame.getPlatform().getId() == null
                    ? null : platformRepository.getReferenceById(videogame.getPlatform().getId()));
            entity.setDigitalPlatformEntity(videogame.getDigitalPlatform() == null || videogame.getDigitalPlatform().getId() == null
                    ? null : digitalPlatformJpa.getReferenceById(videogame.getDigitalPlatform().getId()));
            return mapper.toDomain(entity);
        });
    }
}
