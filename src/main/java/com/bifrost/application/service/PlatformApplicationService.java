package com.bifrost.application.service;

import com.bifrost.application.port.in.PlatformUseCase;
import com.bifrost.application.port.out.PlatformRepositoryPort;
import com.bifrost.domain.Platform;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformApplicationService implements PlatformUseCase {

    private final PlatformRepositoryPort platformRepository;

    public PlatformApplicationService(PlatformRepositoryPort platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Platform> getByName(String name) {
        return platformRepository.findByNameContaining(name);
    }

    @Override
    @Transactional
    public Platform create(Platform platform) {
        return platformRepository.save(platform);
    }
    @Override
    @Transactional
    public Optional<Platform> update(Integer id, Platform platform) {
        return platformRepository.update(id, platform);
    }
}
