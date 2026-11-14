package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.application.port.out.DigitalPlatformRepositoryPort;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Repository
public class DigitalPlatformPersistence implements DigitalPlatformRepositoryPort {

    private final DigitalPlatformJpa digitalPlatformRepository;
    private final PersistenceMapper mapper;

    public DigitalPlatformPersistence(DigitalPlatformJpa digitalPlatformRepository, PersistenceMapper mapper) {
        this.digitalPlatformRepository = digitalPlatformRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DigitalPlatform> findAll() {
        return digitalPlatformRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public DigitalPlatform save(DigitalPlatform digitalPlatform) {
        DigitalPlatformEntity entity = new DigitalPlatformEntity();
        mapper.updateEntity(digitalPlatform, entity);
        return mapper.toDomain(digitalPlatformRepository.save(entity));
    }

    @Override
    public Optional<DigitalPlatform> update(Integer id, UnaryOperator<DigitalPlatform> patch) {
        return digitalPlatformRepository.findById(id).map(entity -> {
            mapper.updateEntity(patch.apply(mapper.toDomain(entity)), entity);
            return mapper.toDomain(entity);
        });
    }

    @Override
    public boolean delete(Integer id) {
        return digitalPlatformRepository.findById(id).map(entity -> {
            digitalPlatformRepository.delete(entity);
            return true;
        }).orElse(false);
    }
}
