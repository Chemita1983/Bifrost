package com.bifrost.application.service;

import com.bifrost.application.port.in.SteamSyncUseCase;
import com.bifrost.application.port.out.SteamLibraryPort;
import com.bifrost.application.port.out.SteamLibraryPort.OwnedGame;
import com.bifrost.application.port.out.VideogameRepositoryPort;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.domain.Platform;
import com.bifrost.domain.Videogame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Imports the Steam library: games are matched by their Steam application ID.
 * New games are created; existing games only get their hours updated. Nothing is ever deleted.
 */
@Service
public class SteamSyncService implements SteamSyncUseCase {

    private final SteamLibraryPort steamLibrary;
    private final VideogameRepositoryPort videogameRepository;
    private final int platformId;
    private final int digitalPlatformId;

    public SteamSyncService(SteamLibraryPort steamLibrary, VideogameRepositoryPort videogameRepository,
                            @Value("${bifrost.steam.platform-id}") int platformId,
                            @Value("${bifrost.steam.digital-platform-id}") int digitalPlatformId) {
        this.steamLibrary = steamLibrary;
        this.videogameRepository = videogameRepository;
        this.platformId = platformId;
        this.digitalPlatformId = digitalPlatformId;
    }

    @Override
    @Transactional
    public Result sync(boolean dryRun) {
        List<OwnedGame> steamGames = steamLibrary.getOwnedGames();
        Map<Integer, Videogame> collection = videogameRepository.findSteamGames().stream()
                .collect(Collectors.toMap(Videogame::getSteamAppId, Function.identity()));

        // 1. Decide what changes.
        List<OwnedGame> newGames = steamGames.stream()
                .filter(game -> !collection.containsKey(game.appId()))
                .toList();
        List<OwnedGame> gamesWithNewHours = steamGames.stream()
                .filter(game -> collection.containsKey(game.appId()))
                .filter(game -> !Objects.equals(collection.get(game.appId()).getHoursPlayed(), hoursPlayed(game)))
                .toList();
        int unchanged = steamGames.size() - newGames.size() - gamesWithNewHours.size();

        // 2. Apply the changes, unless it is only a preview.
        if (!dryRun) {
            newGames.forEach(game -> videogameRepository.save(newVideogame(game)));
            gamesWithNewHours.forEach(game -> updateHours(collection.get(game.appId()).getId(), hoursPlayed(game)));
        }
        return new Result(newGames.size(), gamesWithNewHours.size(), unchanged, dryRun);
    }

    /** Steam reports minutes; partial hours are rounded down. */
    private static int hoursPlayed(OwnedGame game) {
        return game.minutesPlayed() / 60;
    }

    private Videogame newVideogame(OwnedGame game) {
        Videogame videogame = new Videogame();
        videogame.setName(game.name());
        videogame.setPlatform(new Platform(platformId, null, null, null));
        videogame.setDigitalPlatform(new DigitalPlatform(digitalPlatformId, null, null));
        videogame.setDigital(1);
        videogame.setHoursPlayed(hoursPlayed(game));
        videogame.setSteamAppId(game.appId());
        return videogame;
    }

    private void updateHours(Integer videogameId, int hours) {
        videogameRepository.update(videogameId, videogame -> {
            videogame.setHoursPlayed(hours);
            return videogame;
        });
    }
}
