package com.bifrost.aplication.controller;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.service.impl.VideogameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VideogamesController {

    @Autowired
    private VideogameServiceImpl videogameService;

    /*   @GetMapping(value="/getConsoleVideogamesWeb" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public String getVideogamesWeb(Model model) {
        model.addAttribute("outvideogames", videogameService.getConsoleVideogames().toCompletableFuture().join());
        return "index";
    }*/

    @GetMapping(value="/getConsoleVideogame/{name}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutVideogame> getConsoleVideogameByName(@PathVariable("name") String gameName) {
        return videogameService.getConsoleVideogameByName(gameName).toCompletableFuture().join();
    }

    @GetMapping(value="/getConsoleVideogames" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutVideogame> getConsoleVideogames() {
        return videogameService.getConsoleVideogames().toCompletableFuture().join();
    }

    @GetMapping(value="/getPcVideogame/{name}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutVideogame> getPcVideogame(@PathVariable("name") String gameName) {
        return videogameService.getPcVideogameByName(gameName).toCompletableFuture().join();
    }

    @GetMapping(value="/getPcVideogames" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutVideogame> getPcVideogames() {
        return videogameService.getPcVideogames().toCompletableFuture().join();
    }
}
