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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    void filteredSearchConvertsFlagsAndMapsMockedRepositoryResults() {
        when(videogamesRepository.findFiltered(5, 0, 1, false, "Shooter"))
                .thenReturn(List.of(new VideogameEntity()));
        assertThat(adapter.findFiltered(5, false, true, false, "Shooter")).hasSize(1);
        verify(videogamesRepository).findFiltered(5, 0, 1, false, "Shooter");
        assertThat(adapter.findFiltered(null, null, null, null, "rpg")).isEmpty();
        verify(videogamesRepository).findFiltered(null, null, null, null, "rpg");
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
    @Test
    void deleteReturnsTrueWhenEntityExists() {
        VideogameEntity entity = new VideogameEntity();
        when(videogamesRepository.findById(1)).thenReturn(Optional.of(entity));

        assertThat(adapter.delete(1)).isTrue();
        verify(videogamesRepository).delete(entity);
    }

    @Test
    void deleteReturnsFalseWhenEntityIsMissing() {
        when(videogamesRepository.findById(1)).thenReturn(Optional.empty());

        assertThat(adapter.delete(1)).isFalse();
        verify(videogamesRepository, never()).delete(any(VideogameEntity.class));
    }
}
