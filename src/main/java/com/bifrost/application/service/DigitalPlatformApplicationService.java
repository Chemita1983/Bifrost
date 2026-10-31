package com.bifrost.application.service;

import com.bifrost.application.port.in.DigitalPlatformUseCase;
import com.bifrost.application.port.out.DigitalPlatformRepositoryPort;
import com.bifrost.domain.DigitalPlatform;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<DigitalPlatform> update(Integer id, DigitalPlatform digitalPlatform) {
        return digitalPlatformJpa.update(id, digitalPlatform);
    }
}
