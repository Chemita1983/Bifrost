package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.api.OutDigitalPlatform;
import com.bifrost.aplication.api.OutPlatform;
import com.bifrost.aplication.domain.Platform;
import com.bifrost.aplication.exceptions.BadRequestException;
import com.bifrost.aplication.mappers.PlatformBuilder;
import com.bifrost.aplication.repository.DigitalPlatformRepository;
import com.bifrost.aplication.repository.PlatformRepository;
import com.bifrost.aplication.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private DigitalPlatformRepository digitalPlatformRepository;

    @Autowired
    private PlatformBuilder platformBuilder;

    @Override
    public CompletionStage<List<OutPlatform>> getPlatforms() {

        return CompletableFuture.supplyAsync(() -> platformRepository.getPlatforms())
                .thenApplyAsync(platforms -> platformBuilder.convertListPlatform(platforms));
    }

    @Override
    public CompletionStage<String> addPlatform(Platform platform) {

        return CompletableFuture.supplyAsync(() -> platformRepository.save(createPlatform(platform)))
                .thenApplyAsync(result -> "OK " + result.getPkPlatform() + " " + result.getPlatformName())
                .exceptionally(exception -> {
                    throw new BadRequestException(exception.getMessage());
                });
    }

    @Override
    public CompletionStage<List<OutDigitalPlatform>> getDigitalPlatforms() {

        return CompletableFuture.supplyAsync(() -> digitalPlatformRepository.getDigitalPlatforms())
                .thenApplyAsync(digitalPlatforms -> platformBuilder.convertListDigitalPlatform(digitalPlatforms));
    }

    private Platform createPlatform(Platform platform){

        return Platform.builder()
                .pkPlatform(platform.getPkPlatform())
                .platformName(platform.getPlatformName())
                .platformCompany(platform.getPlatformCompany())
                .platformYear(platform.getPlatformYear())
                .build();
    }
}

