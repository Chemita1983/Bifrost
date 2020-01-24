package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.OutPlatform;
import com.bifrost.aplication.mappers.PlatformBuilder;
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
    private PlatformBuilder platformBuilder;

    @Override
    public CompletionStage<List<OutPlatform>> getPlatforms() {
        return CompletableFuture.supplyAsync(() -> platformRepository.getPlatforms())
                .thenApplyAsync(platforms -> platformBuilder.convertListPlatform(platforms));
    }
}

