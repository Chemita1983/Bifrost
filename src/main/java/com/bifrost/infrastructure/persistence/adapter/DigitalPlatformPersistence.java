package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.application.port.out.DigitalPlatformRepositoryPort;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DigitalPlatformPersistence implements DigitalPlatformRepositoryPort {

    private final DigitalPlatformJpa digitalPlatformJpa;
    private final PersistenceMapper mapper;

    public DigitalPlatformPersistence(DigitalPlatformJpa digitalPlatformJpa,
                                             PersistenceMapper mapper) {
        this.digitalPlatformJpa = digitalPlatformJpa;
        this.mapper = mapper;
    }

    @Override
    public List<DigitalPlatform> findAll() {
        return digitalPlatformJpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public DigitalPlatform save(DigitalPlatform digitalPlatform) {
        DigitalPlatformEntity saved = digitalPlatformJpa.save(mapper.toEntity(digitalPlatform));
        return mapper.toDomain(saved);
    }
}
