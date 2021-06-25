package com.bifrost.aplication.controller;

import com.bifrost.aplication.domain.Platform;
import com.bifrost.aplication.service.impl.PlatformServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlatformController {

    @Autowired
    private PlatformServiceImpl platformService;

    @GetMapping(value = "/getAllPlatforms", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Platform> getPlatformById() {
        return platformService.readAll();
    }


    @PostMapping(value = "/addPlatform", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Platform addPlatform(@RequestBody Platform platform) {
        return platformService.add(platform);
    }
}
