package com.bifrost.aplication.controller;

import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.service.impl.VideogameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class VideogamesController {

    @Autowired
    private VideogameServiceImpl videogameService;

    @GetMapping(value="/getVideogamesWeb" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public String getVideogamesWeb(Model model) {
        model.addAttribute("outvideogames", videogameService.getAllVideogames().toCompletableFuture().join());
        return "index";
    }

    @GetMapping(value="/getVideogame/{name}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutVideogame> getVideogame(@PathVariable("name") String gameName) {
        return videogameService.getVideogameByName(gameName).toCompletableFuture().join();
    }

    @GetMapping(value="/getVideogames" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutVideogame> getVideogames(Model model) {
        return videogameService.getAllVideogames().toCompletableFuture().join();
    }
}
