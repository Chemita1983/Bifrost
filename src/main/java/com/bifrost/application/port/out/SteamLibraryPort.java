package com.bifrost.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/** Reads the games owned by the configured Steam account. */
public interface SteamLibraryPort {

    List<OwnedGame> getOwnedGames();

    /**
     * Returns the app IDs that the Steam store identifies as software, such as Wallpaper Engine.
     * Apps unknown to the store are not included.
     */
    Set<Integer> findSoftware(List<Integer> appIds);

    /**
     * Returns the cover image URL that the Steam store shows for the app.
     * Never fails: it is empty when the store cannot be reached or has no valid image.
     */
    Optional<String> findCoverUrl(int appId);

    record OwnedGame(int appId, String name, int minutesPlayed) {
    }
}
