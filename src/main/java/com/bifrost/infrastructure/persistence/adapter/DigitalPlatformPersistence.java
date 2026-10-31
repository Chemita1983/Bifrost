package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.application.port.out.DigitalPlatformRepositoryPort;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    @Override
    public Optional<DigitalPlatform> update(Integer id, DigitalPlatform digitalPlatform) {
        return digitalPlatformJpa.findById(id).map(entity -> {
            entity.setDigitalPlatformName(digitalPlatform.getName());
            entity.setDigitalPlatformCompany(digitalPlatform.getCompany());
            return mapper.toDomain(entity);
        });
    }
}
