package com.bifrost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VideogamesController {

    @GetMapping("/videogame")
    public String main(@RequestParam(name="name", required=false, defaultValue="Bifröst") String name, Model model) {
        model.addAttribute("name", name);
        return "videogame";
    }
}
