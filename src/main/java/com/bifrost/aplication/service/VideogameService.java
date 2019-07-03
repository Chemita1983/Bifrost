package com.bifrost.aplication.service;

import com.bifrost.aplication.domain.OutVideogame;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Service
public interface VideogameService {

    CompletionStage<List<OutVideogame>> getAllVideogames();

    CompletionStage<List<OutVideogame>> getVideogameByName(String gameName);
}