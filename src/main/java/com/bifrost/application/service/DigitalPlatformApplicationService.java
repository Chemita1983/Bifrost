package com.bifrost.application.service;

import com.bifrost.application.port.in.DigitalPlatformUseCase;
import com.bifrost.application.port.out.DigitalPlatformRepositoryPort;
import com.bifrost.domain.DigitalPlatform;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Service
public class DigitalPlatformApplicationService implements DigitalPlatformUseCase {

    private final DigitalPlatformRepositoryPort digitalPlatformJpa;

    public DigitalPlatformApplicationService(DigitalPlatformRepositoryPort digitalPlatformJpa) {
        this.digitalPlatformJpa = digitalPlatformJpa;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DigitalPlatform> getAll() {
        return digitalPlatformJpa.findAll();
    }

    @Override
    @Transactional
    public DigitalPlatform create(DigitalPlatform digitalPlatform) {
        return digitalPlatformJpa.save(digitalPlatform);
    }
    @Override
    @Transactional
    public Optional<DigitalPlatform> update(Integer id, UnaryOperator<DigitalPlatform> patch) {
        return digitalPlatformJpa.update(id, patch);
    }
    @Override
    @Transactional
    public boolean delete(Integer id) {
        return digitalPlatformJpa.delete(id);
    }
}
