package com.bifrost.aplication.service;

import com.bifrost.aplication.api.OutDigitalPlatform;
import com.bifrost.aplication.api.OutPlatform;
import com.bifrost.aplication.domain.Platform;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface PlatformService {

    CompletionStage<List<OutPlatform>> getPlatforms();

    CompletionStage<List<OutDigitalPlatform>> getDigitalPlatforms();

    CompletionStage<String> addPlatform(Platform platform);
}
