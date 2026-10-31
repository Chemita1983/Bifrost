package com.bifrost.application.port.in;

import com.bifrost.domain.Platform;

import java.util.List;
import java.util.Optional;

public interface PlatformUseCase {

    List<Platform> getAll();

    List<Platform> getByName(String name);

    Platform create(Platform platform);
    Optional<Platform> update(Integer id, Platform platform);
}
