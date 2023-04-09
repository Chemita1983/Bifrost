package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.PlatformEntity;
import com.bifrost.aplication.repository.PlatformRepository;
import com.bifrost.aplication.service.BifrostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformServiceImpl implements BifrostService<PlatformEntity> {

    @Autowired
    private PlatformRepository platformRepository;

    @Override
    public List<PlatformEntity> readAll() {
        return platformRepository.getPlatforms();
    }

    @Override
    public List<PlatformEntity> readByName(String name) {
        return platformRepository.getPlatformByName(name);
    }

    @Override
    public PlatformEntity add(PlatformEntity platformEntity) {
        return platformRepository.save(platformEntity);
    }
}

