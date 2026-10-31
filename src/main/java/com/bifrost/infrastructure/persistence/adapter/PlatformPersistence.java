package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.application.port.out.PlatformRepositoryPort;
import com.bifrost.domain.Platform;
import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.repository.PlatformJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlatformPersistence implements PlatformRepositoryPort {

    private final PlatformJpa platformRepository;
    private final PersistenceMapper mapper;

    public PlatformPersistence(PlatformJpa platformRepository, PersistenceMapper mapper) {
        this.platformRepository = platformRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Platform> findAll() {
        return platformRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Platform> findByNameContaining(String name) {
        return platformRepository.findByPlatformNameContaining(name).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public Platform save(Platform platform) {
        PlatformEntity saved = platformRepository.save(mapper.toEntity(platform));
        return mapper.toDomain(saved);
    }
    @Override
    public Optional<Platform> update(Integer id, Platform platform) {
        return platformRepository.findById(id).map(entity -> {
            mapper.updateEntity(platform, entity);
            return mapper.toDomain(entity);
        });
    }
}
