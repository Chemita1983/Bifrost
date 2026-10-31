package com.bifrost.application.port.in;

import com.bifrost.domain.DigitalPlatform;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface DigitalPlatformUseCase {

    List<DigitalPlatform> getAll();

    DigitalPlatform create(DigitalPlatform digitalPlatform);
    Optional<DigitalPlatform> update(Integer id, UnaryOperator<DigitalPlatform> patch);
    boolean delete(Integer id);
}
