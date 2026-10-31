package com.bifrost.application.port.out;

import com.bifrost.domain.Platform;

import java.util.List;
import java.util.Optional;

public interface PlatformRepositoryPort {

    List<Platform> findAll();

    List<Platform> findByNameContaining(String name);

    Platform save(Platform platform);
    Optional<Platform> update(Integer id, Platform platform);
}
