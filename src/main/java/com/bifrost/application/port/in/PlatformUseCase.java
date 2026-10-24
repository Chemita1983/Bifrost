package com.bifrost.application.port.in;

import com.bifrost.domain.Platform;

import java.util.List;

public interface PlatformUseCase {

    List<Platform> getAll();

    List<Platform> getByName(String name);

    Platform create(Platform platform);
}
