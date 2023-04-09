package com.bifrost.aplication.controller;

import com.bifrost.aplication.annotations.ValidPlatform;
import com.bifrost.aplication.domain.PlatformEntity;
import com.bifrost.aplication.service.impl.PlatformServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class PlatformController {

    private static final String PATH = "platform";

    @Autowired
    private PlatformServiceImpl platformService;

    @GetMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlatformEntity> getPlatformById() {
        return platformService.readAll();
    }

    @PostMapping(value = PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlatformEntity addPlatform(@RequestBody @ValidPlatform PlatformEntity platformEntity) {
        return platformService.add(platformEntity);
    }
}
