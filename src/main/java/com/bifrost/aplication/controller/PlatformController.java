package com.bifrost.aplication.controller;

import com.bifrost.aplication.api.OutDigitalPlatform;
import com.bifrost.aplication.api.OutPlatform;
import com.bifrost.aplication.domain.Platform;
import com.bifrost.aplication.service.impl.PlatformServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
public class PlatformController {

    @Autowired
    private PlatformServiceImpl platformService;

    @GetMapping(value = "/getAllPlatforms", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<List<OutPlatform>> getPlatform() {
        return platformService.getPlatforms();
    }

    @GetMapping(value = "/getAllDigitalPlatforms", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<List<OutDigitalPlatform>> getDigitalPlatform() {
        return platformService.getDigitalPlatforms();
    }

    @PostMapping(value = "/addPlatform", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<String> addPlatform(@RequestBody Platform platform) {
        return platformService.addPlatform(platform);
    }
}
