package com.bifrost.aplication.service;

import com.bifrost.aplication.domain.OutPlatform;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;


@Service
public interface PlatformService {

    CompletionStage<List<OutPlatform>> getPlatforms();
}
