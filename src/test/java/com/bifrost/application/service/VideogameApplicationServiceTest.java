package com.bifrost.application.service;

import com.bifrost.application.port.out.VideogameRepositoryPort;
import com.bifrost.application.service.VideogameApplicationService;
import com.bifrost.domain.Videogame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideogameApplicationServiceTest {

    @Mock
    private VideogameRepositoryPort videogameRepository;

    private VideogameApplicationService service;

    @BeforeEach
    void setUp() {
        service = new VideogameApplicationService(videogameRepository);
    }

    @Test
    void shouldGetAllVideogames() {
        List<Videogame> videogames = List.of(new Videogame());
        when(videogameRepository.findAll()).thenReturn(videogames);

        List<Videogame> result = service.getAll();

        assertThat(result).isSameAs(videogames);
        verify(videogameRepository).findAll();
    }

    @Test
    void shouldGetVideogamesByName() {
        List<Videogame> videogames = List.of(new Videogame());
        when(videogameRepository.findByNameContaining("zelda")).thenReturn(videogames);

        List<Videogame> result = service.getByName("zelda");

        assertThat(result).isSameAs(videogames);
        verify(videogameRepository).findByNameContaining("zelda");
    }

    @Test
    void shouldCreateVideogame() {
        Videogame videogame = new Videogame();
        when(videogameRepository.save(videogame)).thenReturn(videogame);

        Videogame result = service.create(videogame);

        assertThat(result).isSameAs(videogame);
        verify(videogameRepository).save(videogame);
    }
}
