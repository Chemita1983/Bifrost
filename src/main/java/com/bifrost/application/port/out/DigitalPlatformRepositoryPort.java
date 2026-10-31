package com.bifrost.application.port.out;

import com.bifrost.domain.DigitalPlatform;

import java.util.List;
import java.util.Optional;

public interface DigitalPlatformRepositoryPort {

    List<DigitalPlatform> findAll();

    DigitalPlatform save(DigitalPlatform digitalPlatform);
    Optional<DigitalPlatform> update(Integer id, DigitalPlatform digitalPlatform);
}
