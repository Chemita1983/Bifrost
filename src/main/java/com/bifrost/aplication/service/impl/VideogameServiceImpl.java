package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.api.OutVideogame;
import com.bifrost.aplication.domain.DigitalPlatform;
import com.bifrost.aplication.domain.Platform;
import com.bifrost.aplication.domain.Videogame;
import com.bifrost.aplication.exceptions.BadRequestException;
import com.bifrost.aplication.mappers.VideogameBuilder;
import com.bifrost.aplication.repository.VideogamesRepository;
import com.bifrost.aplication.service.VideogameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class VideogameServiceImpl implements VideogameService {

    @Autowired
    private VideogameBuilder videogameBuilder;

    @Autowired
    private VideogamesRepository videogamesRepository;

    @Override
    public CompletionStage<List<OutVideogame>> getVideogames() {
        return CompletableFuture.supplyAsync(() -> videogamesRepository.getVideogames())
                .thenApplyAsync(v -> videogameBuilder.convertListConsoleVideogame(v));
    }

    @Override
    public CompletionStage<List<OutVideogame>> getVideogameByName(String gameName) {
        return CompletableFuture.supplyAsync(() -> videogamesRepository.getVideogameByName(gameName))
                .thenApplyAsync(v -> videogameBuilder.convertListConsoleVideogame(v));
    }

    @Override
    public CompletionStage<String> addVideogame(Videogame videogame) {

        return CompletableFuture.supplyAsync(() -> videogamesRepository.save(createVideogame(videogame)))
                .thenApplyAsync(result -> "OK " + result.getIdVideogame() + " " + result.getVideogameName())
                .exceptionally(exception -> {
                    throw new BadRequestException(exception.getMessage());
                });
    }

    private Videogame createVideogame(Videogame videogame) {

        return Videogame.builder()
                .videogameName(videogame.getVideogameName())
                .videogameType(videogame.getVideogameType())
                .digitalPlatform(createDigitalPlatform(videogame.getDigitalPlatform()))
                .videogameYear(videogame.getVideogameYear())
                .platform(createPlatform(videogame.getPlatform()))
                .companyName(videogame.getCompanyName())
                .isCompleted(videogame.getIsCompleted())
                .isDigital(videogame.getIsDigital())
                .isPlatinum(videogame.getIsPlatinum())
                .build();
    }

    private Platform createPlatform(Platform platform) {

        return Platform.builder()
                .pkPlatform(platform.getPkPlatform())
                .platformName(platform.getPlatformName())
                .platformCompany(platform.getPlatformCompany())
                .platformYear(platform.getPlatformYear())
                .build();
    }

    private DigitalPlatform createDigitalPlatform(DigitalPlatform digitalPlatform) {

        return Optional.ofNullable(digitalPlatform)
                .map(digitalPlatformResult -> DigitalPlatform.builder()
                        .pkDigitalPlatform(digitalPlatformResult.getPkDigitalPlatform())
                        .digitalPlatformName(digitalPlatformResult.getDigitalPlatformName())
                        .digitalPlatformCompany(digitalPlatformResult.getDigitalPlatformCompany())
                        .build())
                .orElse(null);
    }

}
