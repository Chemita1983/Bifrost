package com.bifrost.application.port.out;

import com.bifrost.domain.Platform;

import java.util.List;

public interface PlatformRepositoryPort {

    List<Platform> findAll();

    List<Platform> findByNameContaining(String name);

    Platform save(Platform platform);
}
