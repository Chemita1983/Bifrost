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
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
                        "Nintendo", 0, 0, 0, false));

        assertThat(result).isNotNull();
        verify(videogameUseCase).create(any(Videogame.class));
    }

    @Test
    void shouldUpdateVideogame() {
        Videogame current = new Videogame(1, "Mario", "Platform", "2020", null, null, "Nintendo", 0, 0, 0, false);
        when(videogameUseCase.update(eq(1), any())).thenAnswer(invocation -> {
            UnaryOperator<Videogame> update = invocation.getArgument(1);
            return Optional.of(update.apply(current));
        });

        VideogameResponse result = controller.updateVideogame(1, Map.of("videogameName", "Luigi"));

        assertThat(result.videogameName()).isEqualTo("Luigi");
        assertThat(result.videogameType()).isEqualTo("Platform");
        verify(videogameUseCase).update(eq(1), any());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingVideogame() {
        when(videogameUseCase.update(eq(1), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.updateVideogame(1, Map.of("videogameName", "Luigi")))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> assertThat(((ResponseStatusException) exception).getStatusCode())
                        .isEqualTo(HttpStatus.NOT_FOUND));
        verify(videogameUseCase).update(eq(1), any());
    }
}
