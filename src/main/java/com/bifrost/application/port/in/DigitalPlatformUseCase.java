package com.bifrost.application.port.in;

import com.bifrost.domain.DigitalPlatform;

import java.util.List;
import java.util.Optional;

public interface DigitalPlatformUseCase {

    List<DigitalPlatform> getAll();

    DigitalPlatform create(DigitalPlatform digitalPlatform);
    Optional<DigitalPlatform> update(Integer id, DigitalPlatform digitalPlatform);
}
