package com.bifrost.aplication.service;

import com.bifrost.aplication.api.OutDigitalPlatform;
import com.bifrost.aplication.api.OutPlatform;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;


@Service
public interface PlatformService {

    CompletionStage<List<OutPlatform>> getPlatforms();

    CompletionStage<List<OutDigitalPlatform>> getDigitalPlatforms();
}
