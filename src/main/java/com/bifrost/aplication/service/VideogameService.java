package com.bifrost.aplication.service;

import com.bifrost.aplication.api.OutVideogame;
import com.bifrost.aplication.domain.Videogame;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface VideogameService {

    CompletionStage<List<OutVideogame>> getVideogames();

    CompletionStage<List<OutVideogame>> getVideogameByName(String gameName);

    CompletionStage<String> addVideogame(Videogame videogame);

}
