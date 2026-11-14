package com.bifrost.application.port.out;

import java.util.List;
import java.util.Set;

/** Reads the games owned by the configured Steam account. */
public interface SteamLibraryPort {

    List<OwnedGame> getOwnedGames();

    /**
     * Returns the app IDs that the Steam store identifies as something other than a game
     * (software, DLC, demos...). Apps unknown to the store are not included.
     */
    Set<Integer> findNonGames(List<Integer> appIds);

    record OwnedGame(int appId, String name, int minutesPlayed) {
    }
}
