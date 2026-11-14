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
        List<OwnedGame> ownedGames = steamLibrary.getOwnedGames();
        Map<Integer, Videogame> collection = videogameRepository.findSteamGames().stream()
                .collect(Collectors.toMap(Videogame::getSteamAppId, Function.identity()));

        // The map is also updated during the loop, so an app ID repeated by Steam is not created twice.
        int created = 0;
        int updated = 0;
        int unchanged = 0;
        for (OwnedGame game : ownedGames) {
            int hours = game.minutesPlayed() / 60;
            Videogame current = collection.get(game.appId());
            if (current == null) {
                Videogame newGame = newSteamGame(game, hours);
                if (!dryRun) {
                    videogameRepository.save(newGame);
                }
                collection.put(game.appId(), newGame);
                created++;
            } else if (Objects.equals(current.getHoursPlayed(), hours)) {
                unchanged++;
            } else {
                if (!dryRun) {
                    videogameRepository.update(current.getId(), videogame -> {
                        videogame.setHoursPlayed(hours);
                        return videogame;
                    });
                }
                current.setHoursPlayed(hours);
                updated++;
            }
        }
        return new Result(created, updated, unchanged, dryRun);
    }

    private Videogame newSteamGame(OwnedGame game, int hours) {
        Videogame videogame = new Videogame();
        videogame.setName(game.name());
        videogame.setPlatform(new Platform(platformId, null, null, null));
        videogame.setDigitalPlatform(new DigitalPlatform(digitalPlatformId, null, null));
        videogame.setDigital(1);
        videogame.setHoursPlayed(hours);
        videogame.setSteamAppId(game.appId());
        return videogame;
    }
}
