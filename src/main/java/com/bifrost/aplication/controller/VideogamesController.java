package com.bifrost.aplication.controller;

import com.bifrost.aplication.domain.OutDigitalPlatform;
import com.bifrost.aplication.domain.OutPlatform;
import com.bifrost.aplication.domain.OutVideogame;
import com.bifrost.aplication.entity.Videogame;
import com.bifrost.aplication.service.impl.PlatformServiceImpl;
import com.bifrost.aplication.service.impl.VideogameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
public class VideogamesController {

    @Autowired
    private VideogameServiceImpl videogameService;

    @Autowired
    private PlatformServiceImpl platformService;

  /*  @GetMapping(value="/getVideogamesWeb" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public String getVideogamesWeb(Model model) {
        model.addAttribute("outvideogames", getVideogames());
        return "index";
    }

  */

    @GetMapping(value = "/getVideogame/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<List<OutVideogame>> getVideogameByName(@PathVariable("name") String gameName) {
        return videogameService.getVideogameByName(gameName);
    }

    @GetMapping(value = "/getVideogames", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<List<OutVideogame>> getVideogames() {
        return videogameService.getVideogames();
    }

    @PostMapping(value = "/addVideogame", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<String> addVideogame(@RequestBody Videogame videogame) {
        return videogameService.addVideogame(videogame);
    }

    @GetMapping(value = "/getPlatform", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<List<OutPlatform>> getPlatform() {
        return platformService.getPlatforms();
    }

    @GetMapping(value = "/getDigitalPlatform", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<List<OutDigitalPlatform>> getDigitalPlatform() {
        return platformService.getDigitalPlatforms();
    }
}
