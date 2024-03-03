package com.bifrost.infrastructure.configuration;


import com.bifrost.aplication.usecase.VideogamesUseCase;
import com.bifrost.domain.ports.VideogamesPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BifrostConfig {

    @Bean
    public VideogamesUseCase videogamesUseCase(VideogamesPort videogamesPort){
        return new VideogamesUseCase(videogamesPort);
    }
}
