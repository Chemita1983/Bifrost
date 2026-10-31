package com.bifrost.application.port.out;

import com.bifrost.domain.DigitalPlatform;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface DigitalPlatformRepositoryPort {

    List<DigitalPlatform> findAll();

    DigitalPlatform save(DigitalPlatform digitalPlatform);
    Optional<DigitalPlatform> update(Integer id, UnaryOperator<DigitalPlatform> patch);
}
