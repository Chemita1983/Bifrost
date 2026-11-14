package com.bifrost.application.port.in;

public interface SteamSyncUseCase {

    /**
     * Adds the Steam games that are not in the collection and updates the hours of the ones that are.
     * Software, such as Wallpaper Engine, is skipped; games and DLC are imported.
     * With {@code dryRun}, only reports what would change without saving anything.
     */
    Result sync(boolean dryRun);

    record Result(int created, int updated, int unchanged, int skipped, boolean dryRun) {
    }
}
