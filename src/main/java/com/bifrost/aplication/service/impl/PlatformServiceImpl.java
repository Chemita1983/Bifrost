package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.Platform;
import com.bifrost.aplication.repository.PlatformRepository;
import com.bifrost.aplication.service.BifrostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformServiceImpl implements BifrostService<Platform> {

    @Autowired
    private PlatformRepository platformRepository;

    @Override
    public List<Platform> readAll() {
        return platformRepository.getPlatforms();
    }

    @Override
    public List<Platform> readByName(String name) {
        return platformRepository.getPlatformByName(name);
    }

    @Override
    public Platform add(Platform platform) {
        return platformRepository.save(platform);
    }
}

