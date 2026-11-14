package com.bifrost.application.service;

import com.bifrost.application.port.in.SteamSyncUseCase.Result;
import com.bifrost.application.port.out.SteamLibraryPort;
import com.bifrost.application.port.out.SteamLibraryPort.OwnedGame;
import com.bifrost.application.port.out.VideogameRepositoryPort;
import com.bifrost.domain.Videogame;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SteamSyncServiceTest {

    private final SteamLibraryPort steamLibrary = mock(SteamLibraryPort.class);
    private final VideogameRepositoryPort repository = mock(VideogameRepositoryPort.class);
    private final SteamSyncService service = new SteamSyncService(steamLibrary, repository, 6, 1);

    @Test
    void createsMissingGamesUpdatesChangedHoursAndKeepsTheRest() {
        when(steamLibrary.getOwnedGames()).thenReturn(List.of(
                new OwnedGame(10, "New game", 125),
                new OwnedGame(20, "Played more", 600),
                new OwnedGame(30, "Same hours", 61)));
        when(repository.findSteamGames()).thenReturn(List.of(steamGame(2, 20, 5), steamGame(3, 30, 1)));
        Videogame stored = steamGame(2, 20, 5);
        stored.setCompleted(1);
        when(repository.update(eq(2), any())).thenAnswer(invocation -> {
            UnaryOperator<Videogame> patch = invocation.getArgument(1);
            return Optional.of(patch.apply(stored));
        });

        Result result = service.sync(false);

        assertThat(result).isEqualTo(new Result(1, 1, 1, 0, false));
        ArgumentCaptor<Videogame> created = ArgumentCaptor.forClass(Videogame.class);
        verify(repository).save(created.capture());
        Videogame game = created.getValue();
        assertThat(game.getId()).isNull();
        assertThat(game.getName()).isEqualTo("New game");
        assertThat(game.getSteamAppId()).isEqualTo(10);
        assertThat(game.getHoursPlayed()).isEqualTo(2);
        assertThat(game.getPlatform().getId()).isEqualTo(6);
        assertThat(game.getDigitalPlatform().getId()).isEqualTo(1);
        assertThat(game.getDigital()).isEqualTo(1);
        assertThat(game.getCompleted()).isZero();
        assertThat(game.getPlatinum()).isZero();
        // Only the hours change; data entered manually is kept.
        assertThat(stored.getHoursPlayed()).isEqualTo(10);
        assertThat(stored.getCompleted()).isEqualTo(1);
        verify(repository, never()).update(eq(3), any());
    }

    @Test
    void dryRunCountsChangesWithoutSaving() {
        when(steamLibrary.getOwnedGames()).thenReturn(List.of(new OwnedGame(10, "New", 0), new OwnedGame(20, "Old", 120)));
        when(repository.findSteamGames()).thenReturn(List.of(steamGame(2, 20, null)));

        assertThat(service.sync(true)).isEqualTo(new Result(1, 1, 0, 0, true));

        verify(repository, never()).save(any());
        verify(repository, never()).update(anyInt(), any());
    }

    @Test
    void hoursAreRoundedDownAndUnknownHoursGetTheSteamValue() {
        when(steamLibrary.getOwnedGames()).thenReturn(List.of(new OwnedGame(10, "New", 59), new OwnedGame(20, "Old", 179)));
        when(repository.findSteamGames()).thenReturn(List.of(steamGame(2, 20, null)));
        Videogame stored = steamGame(2, 20, null);
        when(repository.update(eq(2), any())).thenAnswer(invocation -> {
            UnaryOperator<Videogame> patch = invocation.getArgument(1);
            return Optional.of(patch.apply(stored));
        });

        assertThat(service.sync(false)).isEqualTo(new Result(1, 1, 0, 0, false));

        ArgumentCaptor<Videogame> created = ArgumentCaptor.forClass(Videogame.class);
        verify(repository).save(created.capture());
        assertThat(created.getValue().getHoursPlayed()).isZero();
        assertThat(stored.getHoursPlayed()).isEqualTo(2);
    }

    @Test
    void steamErrorsStopTheSyncBeforeReadingOrChangingTheCollection() {
        when(steamLibrary.getOwnedGames()).thenThrow(new IllegalStateException("Steam unavailable"));

        assertThatThrownBy(() -> service.sync(false)).hasMessage("Steam unavailable");

        verify(repository, never()).findSteamGames();
        verify(repository, never()).save(any());
    }

    @Test
    void appsThatAreNotGamesAreSkippedAndOnlyNewAppsAreCheckedInTheStore() {
        when(steamLibrary.getOwnedGames()).thenReturn(List.of(
                new OwnedGame(620, "Portal 2", 60), new OwnedGame(431960, "Wallpaper Engine", 600),
                new OwnedGame(400, "Portal", 0)));
        when(repository.findSteamGames()).thenReturn(List.of(steamGame(1, 400, 0)));
        when(steamLibrary.findNonGames(List.of(620, 431960))).thenReturn(Set.of(431960));

        assertThat(service.sync(false)).isEqualTo(new Result(1, 0, 1, 1, false));

        ArgumentCaptor<Videogame> created = ArgumentCaptor.forClass(Videogame.class);
        verify(repository).save(created.capture());
        assertThat(created.getValue().getSteamAppId()).isEqualTo(620);
    }

    @Test
    void theStoreIsNotAskedWhenThereAreNoNewApps() {
        when(steamLibrary.getOwnedGames()).thenReturn(List.of(new OwnedGame(400, "Portal", 0)));
        when(repository.findSteamGames()).thenReturn(List.of(steamGame(1, 400, 0)));

        assertThat(service.sync(false)).isEqualTo(new Result(0, 0, 1, 0, false));

        verify(steamLibrary, never()).findNonGames(any());
    }

    private static Videogame steamGame(int id, int appId, Integer hours) {
        Videogame game = new Videogame();
        game.setId(id);
        game.setSteamAppId(appId);
        game.setHoursPlayed(hours);
        return game;
    }
}
