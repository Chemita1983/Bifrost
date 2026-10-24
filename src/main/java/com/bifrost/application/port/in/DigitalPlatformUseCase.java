package com.bifrost.application.port.in;

import com.bifrost.domain.DigitalPlatform;

import java.util.List;

public interface DigitalPlatformUseCase {

    List<DigitalPlatform> getAll();

    DigitalPlatform create(DigitalPlatform digitalPlatform);
}
