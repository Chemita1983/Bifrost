package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.VideogameUseCase;
import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.web.dto.VideogameRequest;
import com.bifrost.infrastructure.web.dto.VideogameResponse;
import com.bifrost.infrastructure.mapper.WebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideogameControllerTest {

    @Mock
    private VideogameUseCase videogameUseCase;

    private VideogameController controller;

    @BeforeEach
    void setUp() {
        controller = new VideogameController(videogameUseCase, new WebMapper());
    }

    @Test
    void shouldGetAllVideogames() {
        when(videogameUseCase.getAll()).thenReturn(List.of(new Videogame()));

        List<VideogameResponse> result = controller.getVideogames();

        assertThat(result).hasSize(1);
        verify(videogameUseCase).getAll();
    }

    @Test
    void shouldGetVideogamesByName() {
        when(videogameUseCase.getByName("mario")).thenReturn(List.of(new Videogame()));

        List<VideogameResponse> result = controller.getVideogameByName("mario");

        assertThat(result).hasSize(1);
        verify(videogameUseCase).getByName("mario");
    }

    @Test
    void shouldCreateVideogame() {
        when(videogameUseCase.create(any(Videogame.class))).thenReturn(new Videogame());

        VideogameResponse result = controller.addVideogame(
                new VideogameRequest("Mario", "Platform", "2020", null, null,
                        "Nintendo", 0, 0, 0, 1));

        assertThat(result).isNotNull();
        verify(videogameUseCase).create(any(Videogame.class));
    }
}
