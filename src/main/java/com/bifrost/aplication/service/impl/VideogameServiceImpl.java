package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.mappers.VideogameBuilder;
import com.bifrost.aplication.repository.VideogamesConsoleRepository;
import com.bifrost.aplication.repository.VideogamesPcRepository;
import com.bifrost.aplication.service.VideogameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class VideogameServiceImpl implements VideogameService {

    @Autowired
    private VideogameBuilder videogameBuilder;

    @Autowired
    private VideogamesConsoleRepository videogamesConsoleRepository;

    @Autowired
    private VideogamesPcRepository videogamesPcRepository;

    @Override
    public CompletionStage<List<OutVideogame>> getConsoleVideogames() {
        return CompletableFuture.supplyAsync(() -> videogamesConsoleRepository.getConsoleVideogames())
                .thenApplyAsync(v -> videogameBuilder.convertListConsoleVideogame(v));
    }

    @Override
    public CompletionStage<List<OutVideogame>> getConsoleVideogameByName(String gameName) {
        return CompletableFuture.supplyAsync(() -> videogamesConsoleRepository.getConsoleVideogameByName(gameName))
                .thenApplyAsync(v -> videogameBuilder.convertListConsoleVideogame(v));
    }

    @Override
    public CompletionStage<List<OutVideogame>> getPcVideogames() {
        return CompletableFuture.supplyAsync(() -> videogamesPcRepository.getPcVideogames())
                .thenApplyAsync(v -> videogameBuilder.convertListPcVideogames(v));
    }

    @Override
    public CompletionStage<List<OutVideogame>> getPcVideogameByName(String gameName) {
        return CompletableFuture.supplyAsync(() -> videogamesPcRepository.getPcVideogameByName(gameName))
                .thenApplyAsync(v -> videogameBuilder.convertListPcVideogames(v));
    }

}
