package com.bifrost.application.port.out;

import com.bifrost.domain.DigitalPlatform;

import java.util.List;

public interface DigitalPlatformRepositoryPort {

    List<DigitalPlatform> findAll();

    DigitalPlatform save(DigitalPlatform digitalPlatform);
}
