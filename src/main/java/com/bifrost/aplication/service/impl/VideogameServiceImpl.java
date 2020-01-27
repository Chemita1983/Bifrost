package com.bifrost.aplication.service.impl;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.entity.Videogame;
import com.bifrost.aplication.mappers.VideogameBuilder;
import com.bifrost.aplication.repository.VideogamesRepository;
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
        return CompletableFuture.supplyAsync(() -> videogamesRepository.save(videogame))
                .thenApplyAsync(result -> "OK " + result.getIdVideogame()+ " " + result.getVideogameName())
                .exceptionally(e -> "KO " + e.getMessage());
    }

}
