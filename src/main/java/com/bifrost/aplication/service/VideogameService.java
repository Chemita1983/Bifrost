package com.bifrost.aplication.service;

import com.bifrost.aplication.domain.OutVideogame;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public interface VideogameService {

    CompletionStage<List<OutVideogame>> getConsoleVideogames();

    CompletionStage<List<OutVideogame>> getConsoleVideogameByName(String gameName);

    CompletionStage<List<OutVideogame>> getPcVideogames();

    CompletionStage<List<OutVideogame>> getPcVideogameByName(String gameName);
}
