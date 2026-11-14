package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.SteamSyncUseCase;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SteamSyncController {

    private final SteamSyncUseCase steamSyncUseCase;

    public SteamSyncController(SteamSyncUseCase steamSyncUseCase) {
        this.steamSyncUseCase = steamSyncUseCase;
    }

    /** {@code POST /steam/sync?dryRun=true} shows what would change without saving anything. */
    @PostMapping(value = "steam/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public SteamSyncUseCase.Result sync(@RequestParam(value = "dryRun", defaultValue = "false") boolean dryRun) {
        return steamSyncUseCase.sync(dryRun);
    }
}
