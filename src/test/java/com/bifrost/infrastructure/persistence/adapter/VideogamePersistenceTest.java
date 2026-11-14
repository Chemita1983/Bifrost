package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.domain.Videogame;
import com.bifrost.domain.Platform;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
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
    void shouldSaveVideogameWithResolvedRelationsAndWithoutId() {
        Videogame videogame = new Videogame();
        videogame.setId(99);
        videogame.setName("Zelda");
        videogame.setDigital(1);
        videogame.setHoursPlayed(10);
        videogame.setPlatform(new Platform(1, null, null, null));
        videogame.setDigitalPlatform(new DigitalPlatform(2, null, null));
        PlatformEntity platform = new PlatformEntity();
        DigitalPlatformEntity store = new DigitalPlatformEntity();
        when(platformRepository.findById(1)).thenReturn(Optional.of(platform));
        when(digitalPlatformJpa.findById(2)).thenReturn(Optional.of(store));
        when(videogamesRepository.save(any(VideogameEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Videogame result = adapter.save(videogame);

        var saved = org.mockito.ArgumentCaptor.forClass(VideogameEntity.class);
        verify(videogamesRepository).save(saved.capture());
        assertThat(saved.getValue().getId()).isNull();
        assertThat(saved.getValue().getVideogameName()).isEqualTo("Zelda");
        assertThat(saved.getValue().getHoursPlayed()).isEqualTo(10);
        assertThat(saved.getValue().getPlatformEntity()).isSameAs(platform);
        assertThat(saved.getValue().getDigitalPlatformEntity()).isSameAs(store);
        assertThat(result.getName()).isEqualTo("Zelda");
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

    @Test
    void findSteamGamesMapsOnlyGamesWithSteamAppId() {
        VideogameEntity entity = new VideogameEntity();
        entity.setSteamAppId(620);
        when(videogamesRepository.findBySteamAppIdNotNull()).thenReturn(List.of(entity));

        assertThat(adapter.findSteamGames()).singleElement()
                .satisfies(game -> assertThat(game.getSteamAppId()).isEqualTo(620));
    }

    @Test
    void saveStoresSteamAppIdButUpdateNeverChangesIt() {
        PlatformEntity platform = new PlatformEntity();
        platform.setId(7);
        when(platformRepository.findById(7)).thenReturn(Optional.of(platform));
        when(videogamesRepository.save(any(VideogameEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Videogame steamGame = new Videogame();
        steamGame.setName("Portal 2");
        steamGame.setPlatform(new Platform(7, null, null, null));
        steamGame.setSteamAppId(620);

        assertThat(adapter.save(steamGame).getSteamAppId()).isEqualTo(620);

        VideogameEntity entity = new VideogameEntity();
        entity.setPlatformEntity(platform);
        entity.setSteamAppId(620);
        when(videogamesRepository.findById(1)).thenReturn(Optional.of(entity));
        // A PATCH builds the game from the request, which has no steamAppId.
        adapter.update(1, game -> {
            game.setSteamAppId(null);
            return game;
        });
        assertThat(entity.getSteamAppId()).isEqualTo(620);
    }

    @Test
    void updateHoursPlayedOnlyRunsTheHoursQueryAndReportsMissingGames() {
        when(videogamesRepository.updateHoursPlayed(1, 12)).thenReturn(1);
        when(videogamesRepository.updateHoursPlayed(2, 12)).thenReturn(0);

        assertThat(adapter.updateHoursPlayed(1, 12)).isTrue();
        assertThat(adapter.updateHoursPlayed(2, 12)).isFalse();
        // No entity is loaded or saved, so no other column can be rewritten.
        verify(videogamesRepository, never()).findById(any());
        verify(videogamesRepository, never()).save(any(VideogameEntity.class));
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
