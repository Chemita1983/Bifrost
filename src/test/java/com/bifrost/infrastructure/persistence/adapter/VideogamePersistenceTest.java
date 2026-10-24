package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
import com.bifrost.infrastructure.persistence.repository.PlatformJpa;
import com.bifrost.infrastructure.persistence.repository.VideogamesJpa;
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
class VideogamePersistenceTest {

    @Mock
    private VideogamesJpa videogamesRepository;

    @Mock
    private PlatformJpa platformRepository;

    @Mock
    private DigitalPlatformJpa digitalPlatformJpa;

    private VideogamePersistence adapter;

    @BeforeEach
    void setUp() {
        adapter = new VideogamePersistence(videogamesRepository, platformRepository,
                digitalPlatformJpa, new PersistenceMapper());
    }

    @Test
    void shouldFindAllVideogames() {
        VideogameEntity entity = new VideogameEntity();
        List<VideogameEntity> videogames = List.of(entity);
        when(videogamesRepository.findAll()).thenReturn(videogames);

        List<Videogame> result = adapter.findAll();

        assertThat(result).hasSize(1);
        verify(videogamesRepository).findAll();
    }

    @Test
    void shouldFindVideogamesByName() {
        when(videogamesRepository.findByVideogameNameContaining("mario"))
                .thenReturn(List.of(new VideogameEntity()));

        List<Videogame> result = adapter.findByNameContaining("mario");

        assertThat(result).hasSize(1);
        verify(videogamesRepository).findByVideogameNameContaining("mario");
    }

    @Test
    void shouldSaveVideogame() {
        Videogame videogame = new Videogame();
        when(videogamesRepository.save(org.mockito.ArgumentMatchers.any(VideogameEntity.class)))
                .thenReturn(new VideogameEntity());

        Videogame result = adapter.save(videogame);

        assertThat(result).isNotNull();
        verify(videogamesRepository).save(org.mockito.ArgumentMatchers.any(VideogameEntity.class));
    }
}
