package com.bifrost.application.port.out;

import java.util.List;

/** Reads the games owned by the configured Steam account. */
public interface SteamLibraryPort {

    List<OwnedGame> getOwnedGames();

    record OwnedGame(int appId, String name, int minutesPlayed) {
    }
}
