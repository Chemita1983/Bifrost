package com.bifrost.aplication.service;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.entity.Videogame;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public interface VideogameService {

    CompletionStage<List<OutVideogame>> getVideogames();

    CompletionStage<List<OutVideogame>> getVideogameByName(String gameName);

    CompletionStage<String> addVideogame(Videogame videogame);

}
