package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.domain.Videogame;
import com.bifrost.domain.Platform;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        videogame.setPlatform(new Platform(1, null, null, null));
        when(platformRepository.findById(1)).thenReturn(Optional.of(new PlatformEntity()));
        when(videogamesRepository.save(org.mockito.ArgumentMatchers.any(VideogameEntity.class)))
                .thenReturn(new VideogameEntity());

        Videogame result = adapter.save(videogame);

        assertThat(result).isNotNull();
        verify(videogamesRepository).save(org.mockito.ArgumentMatchers.any(VideogameEntity.class));
    }
    @Test
    void saveRejectsMissingAndUnknownRelationsBeforeSaving() {
        Videogame game = new Videogame();
        assertBadRequest(() -> adapter.save(game), "The videogame platform must be specified");
        game.setPlatform(new Platform(1, null, null, null));
        game.setDigital(1);
        assertBadRequest(() -> adapter.save(game), "The digital platform must be specified for digital videogames");
        game.setDigital(0);
        assertBadRequest(() -> adapter.save(game), "The specified platform does not exist");
        when(platformRepository.findById(1)).thenReturn(Optional.of(new PlatformEntity()));
        game.setDigitalPlatform(new DigitalPlatform(2, null, null));
        assertBadRequest(() -> adapter.save(game), "The specified digital platform does not exist");
        verify(videogamesRepository, never()).save(any());
    }

    @Test
    void updateValidatesMergedStateBeforeChangingEntity() {
        VideogameEntity entity = new VideogameEntity();
        when(videogamesRepository.findById(1)).thenReturn(Optional.of(entity));
        assertBadRequest(() -> adapter.update(1, game -> game), "The videogame platform must be specified");
        assertThat(entity.getPlatformEntity()).isNull();
        assertBadRequest(() -> adapter.update(1, game -> {
            game.setPlatform(new Platform(2, null, null, null));
            game.setDigital(1);
            return game;
        }), "The digital platform must be specified for digital videogames");
        assertThat(entity.getPlatformEntity()).isNull();
        assertBadRequest(() -> adapter.update(1, game -> {
            game.setPlatform(new Platform(2, null, null, null));
            return game;
        }), "The specified platform does not exist");
        when(platformRepository.findById(2)).thenReturn(Optional.of(new PlatformEntity()));
        assertBadRequest(() -> adapter.update(1, game -> {
            game.setPlatform(new Platform(2, null, null, null));
            game.setDigitalPlatform(new DigitalPlatform(3, null, null));
            return game;
        }), "The specified digital platform does not exist");
        assertThat(entity.getPlatformEntity()).isNull();
        when(videogamesRepository.findById(404)).thenReturn(Optional.empty());
        assertThat(adapter.update(404, game -> game)).isEmpty();
    }

    @Test
    void updatePreservesAndResolvesExistingPlatform() {
        PlatformEntity platform = new PlatformEntity();
        platform.setId(7);
        VideogameEntity entity = new VideogameEntity();
        entity.setPlatformEntity(platform);
        entity.setVideogameName("Original");
        when(videogamesRepository.findById(1)).thenReturn(Optional.of(entity));
        when(platformRepository.findById(7)).thenReturn(Optional.of(platform));

        Videogame updated = adapter.update(1, game -> {
            game.setName("Updated");
            return game;
        }).orElseThrow();

        assertThat(updated.getName()).isEqualTo("Updated");
        assertThat(updated.getPlatform().getId()).isEqualTo(7);
        assertThat(entity.getPlatformEntity()).isSameAs(platform);
    }

    private void assertBadRequest(Runnable action, String message) {
        assertThatThrownBy(action::run).isInstanceOfSatisfying(ResponseStatusException.class, ex -> {
            assertThat(ex.getStatusCode().value()).isEqualTo(400);
            assertThat(ex.getReason()).isEqualTo(message);
        });
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
